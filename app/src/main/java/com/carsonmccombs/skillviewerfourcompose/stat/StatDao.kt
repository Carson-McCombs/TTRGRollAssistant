package com.carsonmccombs.skillviewerfourcompose.stat

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier
import com.carsonmccombs.skillviewerfourcompose.statmodifier_relationship.StatAsModifier
import com.carsonmccombs.skillviewerfourcompose.statmodifier_relationship.StatWithModifiers
import kotlinx.coroutines.flow.Flow


@Dao
interface StatDao {
    @Upsert
    suspend fun upsert(stat: Stat)

    @Delete
    suspend fun delete(stat: Stat)


    @Query("SELECT * FROM stat ORDER BY id ASC")
    fun getAllStats(): Flow<List<Stat>>

    @MapInfo(keyColumn = "id", valueColumn = "name" )
    @Query("SELECT id,name FROM stat GROUP BY id")
    fun getAllStatNames(): Flow<Map<Int,String>>

    @Query("SELECT * FROM stat WHERE id = :id")
    fun getStat(id: Int): Flow<Stat>

    @Query("SELECT name FROM stat WHERE id = :id")
    fun getStatName(id: Int): Flow<String>

    @Query("SELECT id FROM stat ORDER BY id")
    fun getStatIDs(): Flow<List<Int>>

    @Query("SELECT * FROM statmodifier WHERE id = :id")
    fun getStatModifier(id: Int): Flow<StatModifier>

    @Query("SELECT id FROM statmodifier WHERE statID = :statID ORDER BY id")
    fun getStatModifierIDs(statID: Int): Flow<List<Int>>

    @MapInfo(keyColumn = "id", valueColumn = "name")
    @Query("SELECT id, name FROM stat WHERE id IN (:statIDs) GROUP BY id")
    fun getStatNames(statIDs: List<Int>): Flow<Map<Int,String>>

    @Transaction
    @Query("SELECT * FROM stat WHERE id = :id ORDER BY id ASC")
    fun getStatWithModifiers(id: Int): Flow<StatWithModifiers>

    @Transaction
    @Query("SELECT * FROM stat ORDER BY id ASC")
    fun getAllStatWithModifiers(): Flow<List<StatWithModifiers>>


    @Transaction
    @MapInfo(keyColumn = "parentStatID", valueColumn = "statID")
    @Query("WITH cte_dependents (statID, id, childStatID, depth) AS (" +
            "SELECT statID, id, childStatID, 0 as depth " +
            "FROM statmodifier " +
            "WHERE childStatID != -1 " +
            "UNION ALL " +
            "SELECT mod.statID, mod.id, r.childStatID, r.depth + 1 " +
            "FROM statmodifier AS mod " +
            "INNER JOIN cte_dependents AS r " +
            "ON r.statID = mod.childStatID " +
            ") " +
            "SELECT statID, childStatID as parentStatID FROM cte_dependents " +
            "UNION " +
            "SELECT id AS statID, id as parentStatID FROM stat "
    )
    fun getDependentStats(): Flow<Map<Int, List<Int>>>



/*
    @Transaction
    @MapInfo(keyColumn = "parentStatID", valueColumn = "statID")
    @Query("WITH cte_dependents (statID, id, childStatID, depth) AS (" +
            "SELECT s.id, mod.id, mod.childStatID, 0 as depth " +
            "FROM stat AS s " +
            "LEFT OUTER JOIN statmodifier AS mod " +
            "ON s.id = mod.statID " +
            "UNION ALL " +
            "SELECT mod.statID, mod.id, r.childStatID, r.depth + 1 " +
            "FROM statmodifier AS mod " +
            "INNER JOIN cte_dependents AS r " +
            "ON r.statID != mod.childStatID " +
            ") " +
            "SELECT statID, childStatID as parentStatID " +
            "FROM cte_dependents "
    )
    fun getNonDependentStats(): Flow<Map<Int, List<Int>>>
*/

    @Transaction
    @MapInfo(keyColumn = "statID", valueColumn = "total")
    @Query("WITH cte_dep_stats (id, statID, childStatID) AS ( " +
            "SELECT id, statID, childStatID " +
            "FROM statmodifier " +
            "WHERE childStatID != -1 " +
            "), " +
            "cte_local_totals (statID, local_total) AS (" +
            "SELECT statID, TOTAL (value) AS local_total " +
            "FROM statmodifier " +
            "GROUP BY statID " +
            "), " +
            "cte_foreign_refs (statID, id, childStatID, depth) AS ( " +
            "SELECT statID, id, childStatID, 0 as depth " +
            "FROM cte_dep_stats " +
            "UNION ALL " +
            "SELECT l.statID, l.id, d.childStatID, l.depth + 1 " +
            "FROM cte_dep_stats AS d " +
            "INNER JOIN cte_foreign_refs l " +
            "ON d.statID = l.childStatID " +
            "), " +
            "cte_foreign_totals (statID, foreign_total) AS ( " +
            "SELECT f.statID, l.local_total as foreign_total  FROM cte_local_totals AS l " +
            "LEFT OUTER JOIN cte_foreign_refs f " +
            "ON l.statID = f.childStatID " +
            "), " +
            "cte_totals (statID, total) AS (" +
            "SELECT l.statID, l.local_total + TOTAL (foreign_total) AS total " +
            "FROM cte_local_totals AS l " +
            "LEFT JOIN cte_foreign_totals AS f " +
            "ON l.statID = f.statID " +
            "GROUP BY l.statID " +
            ") " +
            "SELECT statID, total FROM cte_totals")
    fun getStatTotals(): Flow<Map<Int,Int>>


    @Query("SELECT * FROM statmodifier WHERE id = :id")
    fun getStatAsModifier(id: Int): Flow<StatAsModifier>

    //@Query("UPDATE stat SET total = (SELECT TOTAL (mod.value) FROM statmodifier AS mod WHERE mod.statID = :id )")
    //suspend fun updateStatTotal(id: Int)


    //@Query("UPDATE stat SET total = :total WHERE id = :id")
    //suspend fun setStatTotal(total: Int, id:Int)

    @Upsert
    suspend fun upsertStatModifier(statModifier: StatModifier)

    @Delete
    suspend fun deleteStatModifier(statModifier: StatModifier)

    //@Query("SELECT id FROM stat ORDER BY id ASC")
    //fun getAllStatModifierIDs(statID: Int): Flow<List<Int>>

    //@Query("SELECT * FROM statmodifier WHERE statID = :statID AND id = :id")
    //fun getStatModifier(statID: Int, id: Int): Flow<StatModifier>

}