package com.example.workrate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_titles")
data class JobTitle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String
)
