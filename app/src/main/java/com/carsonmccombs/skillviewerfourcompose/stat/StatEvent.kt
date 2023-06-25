package com.carsonmccombs.skillviewerfourcompose.stat

import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier

sealed interface StatEvent {
    data class Delete(val stat: Stat): StatEvent
    data class Upsert(val stat: Stat): StatEvent
    data class UpsertStatModifier(val statModifier: StatModifier): StatEvent
    data class DeleteStatModifier(val statModifier: StatModifier): StatEvent
}