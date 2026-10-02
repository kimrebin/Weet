package com.example.weet.di

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weet.data.local.AppDatabase
import com.example.weet.data.local.dao.ChecklistDao
import com.example.weet.data.local.dao.PersonDao
import com.example.weet.repository.PersonRepository
import com.example.weet.repository.PersonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // 마이그레이션 1 → 2: historyMessage + photoUrl 컬럼 추가
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // ✨ 안전하게 컬럼이 없을 때만 추가하도록 try-catch 사용
            try {
                database.execSQL("ALTER TABLE persons ADD COLUMN photoUrl TEXT")
            } catch (e: SQLiteException) {
                if (!e.message?.contains("duplicate column name")!!) throw e
            }
        }
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weet-db"
        )
            .addMigrations(MIGRATION_1_2) // 마이그레이션 적용
            //.fallbackToDestructiveMigration() // 필요시 사용 (개발 중)
            .build()
    }

    @Provides
    @Singleton
    fun providePersonDao(db: AppDatabase): PersonDao = db.personDao()

    @Provides
    fun provideChecklistDao(db: AppDatabase): ChecklistDao = db.checklistDao()


}