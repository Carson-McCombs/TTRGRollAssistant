package com.carsonmccombs.skillviewerfourcompose.stat

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier

@Database(entities = [Stat::class, StatModifier::class], version = 9, exportSchema = false)
abstract class StatDatabase: RoomDatabase() {
    abstract val dao: StatDao
    //abstract val modifierDao: StatModifierDao
}