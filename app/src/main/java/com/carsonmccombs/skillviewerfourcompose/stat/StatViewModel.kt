package com.carsonmccombs.skillviewerfourcompose.stat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier
import com.carsonmccombs.skillviewerfourcompose.statmodifier_relationship.StatAsModifier
import com.carsonmccombs.skillviewerfourcompose.statmodifier_relationship.StatWithModifiers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class StatViewModel(private val dao: StatDao): ViewModel() {

    private val _statIDs: Flow<List<Int>> = dao.getStatIDs()

    private val _relatedStats: StateFlow<Map<Int, List<Int>>> = dao.getDependentStats().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    //private val _unrelatedStats: StateFlow<Map<Int, List<Int>>> = dao.getNonDependentStats().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())


    val relatedStats = _relatedStats
    val unrelatedStats = _relatedStats.combine(_statIDs){ relStatIDs, ids ->
        getUnrelatedStats(ids, relStatIDs)
    }

    val statTotals: StateFlow<Map<Int,Int>> = dao.getStatTotals().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    val statNames: StateFlow<Map<Int,String>> = dao.getAllStatNames().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())



    private fun getUnrelatedStats(ids: List<Int>, relStatIDs: Map<Int,List<Int>>): Map<Int, List<Int>>{
        val unrelStats = mutableMapOf<Int, List<Int>>()
        ids.forEach { id ->
            val list = mutableListOf<Int>()
            list.addAll(ids)
            list.removeAll(relStatIDs[id]?: emptyList())
            unrelStats[id] = list
        }
        return unrelStats.toMap()
    }


    fun getStatNames(statIDs: List<Int>): Flow<Map<Int,String>>{
        return dao.getStatNames(statIDs)
    }

    fun getStatModifier(id: Int): Flow<StatModifier>{
        return dao.getStatModifier(id)
    }

    fun getStatName(id: Int): Flow<String> {
        return dao.getStatName(id = id)
    }
    fun getStat(id: Int): Flow<Stat> {
        return dao.getStat(id = id)
    }
    fun getStatIDs(): Flow<List<Int>> {
        return dao.getStatIDs()
    }

    fun getStatWithModifiers(statID: Int): Flow<StatWithModifiers>{
        return dao.getStatWithModifiers(statID)
    }


    fun getStatModifierIDs(statID: Int): Flow<List<Int>>{
        return dao.getStatModifierIDs(statID = statID)
    }

    fun getStatAsModifier(id: Int): Flow<StatAsModifier>{
        return dao.getStatAsModifier(id)
    }


    //fun getStatModifier(statID: Int, id: Int): Flow<StatModifier>{
    //    return dao.getStatModifier(statID = statID, id = id)
    //}




    fun onEvent(event: StatEvent){
        when (event){
            is StatEvent.Delete -> viewModelScope.launch { dao.delete(stat = event.stat) }
            is StatEvent.Upsert -> viewModelScope.launch { dao.upsert(stat = event.stat) }
            is StatEvent.DeleteStatModifier -> viewModelScope.launch {
                dao.deleteStatModifier(statModifier = event.statModifier)
                //dao.setStatTotal(event.statModifier.statID, )
            }
            is StatEvent.UpsertStatModifier -> viewModelScope.launch {
                dao.upsertStatModifier(statModifier = event.statModifier)


            }
        }
    }
}
/*
data class StatDependenciesState(
    val statDependencies: Map<Int, List<Int>> = emptyMap()
)

data class StatNonDependenciesState(
    val statNonDependencies: Map<Int, List<Int>> = emptyMap()
)*/
