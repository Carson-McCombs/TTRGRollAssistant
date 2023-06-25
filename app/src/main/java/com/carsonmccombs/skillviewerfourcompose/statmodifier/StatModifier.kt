package com.carsonmccombs.skillviewerfourcompose.statmodifier

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.carsonmccombs.skillviewerfourcompose.stat.Stat


@Entity(foreignKeys = [
    ForeignKey( entity = Stat::class, onDelete = CASCADE,  parentColumns = ["id"], childColumns = ["statID"]),
    ForeignKey( entity = Stat::class, onDelete = CASCADE,  parentColumns = ["id"], childColumns = ["childStatID"])
])
data class StatModifier(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(index = true) val statID: Int = 0,
    @ColumnInfo val name: String = "",
    @ColumnInfo val type: String = "",
    @ColumnInfo val value: Int = 1,
    @ColumnInfo(index = true) val childStatID: Int? = null
)
