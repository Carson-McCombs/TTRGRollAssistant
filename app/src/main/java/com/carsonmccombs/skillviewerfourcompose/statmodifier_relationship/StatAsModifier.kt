package com.carsonmccombs.skillviewerfourcompose.statmodifier_relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.carsonmccombs.skillviewerfourcompose.stat.Stat
import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier

data class StatAsModifier(
    @Embedded val modifier: StatModifier = StatModifier(),
    @Relation(
        parentColumn = "childStatID",
        entityColumn = "id"
    )
    val stat: Stat? = null
)
