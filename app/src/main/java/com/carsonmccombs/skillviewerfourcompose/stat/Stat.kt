package com.carsonmccombs.skillviewerfourcompose.stat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String = "Stat Name"
)
