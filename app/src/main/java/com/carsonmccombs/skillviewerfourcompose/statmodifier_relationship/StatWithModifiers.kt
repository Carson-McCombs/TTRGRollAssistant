package com.carsonmccombs.skillviewerfourcompose.statmodifier_relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.carsonmccombs.skillviewerfourcompose.stat.Stat
import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier


data class StatWithModifiers(
    @Embedded val stat: Stat = Stat(),
    @Relation(
        parentColumn = "id",
        entityColumn = "statID"
    )
    val modifiers: List<StatModifier> = emptyList()
)
