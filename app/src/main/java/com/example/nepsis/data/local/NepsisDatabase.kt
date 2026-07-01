package com.example.nepsis.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nepsis.data.local.entity.*
import com.example.nepsis.data.local.dao.*

@Database(
    entities = [
        ProfileEntity::class,
        DailyMoodEntity::class,
        TestEntity::class,
        TestResultEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class NepsisDatabase : RoomDatabase() {
    abstract fun nepsisDao(): NepsisDao
}
