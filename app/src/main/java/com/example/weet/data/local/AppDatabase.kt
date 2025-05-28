package com.example.weet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weet.data.local.dao.PersonDao
import com.example.weet.data.local.dao.ChecklistDao
import com.example.weet.data.local.entity.PersonEntity
import com.example.weet.data.local.entity.ChecklistResultEntity

@Database(
    entities = [PersonEntity::class, ChecklistResultEntity::class],
    version = 1 // 필요 시 버전 올리기
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun checklistDao(): ChecklistDao
}