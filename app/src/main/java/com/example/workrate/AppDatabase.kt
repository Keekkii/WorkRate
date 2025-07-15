package com.example.workrate.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [JobTitle::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobTitleDao(): JobTitleDao
}
