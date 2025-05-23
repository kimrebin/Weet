package com.example.weet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.relationshiptracker.data.local.dao.PersonDao
import com.example.relationshiptracker.data.local.model.PersonEntity

@Database(entities = [PersonEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}