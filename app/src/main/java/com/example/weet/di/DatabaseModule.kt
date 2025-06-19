package com.example.weet.di

import android.content.Context
import androidx.room.Room
import com.example.weet.data.local.AppDatabase
import com.example.weet.data.local.dao.PersonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.example.weet.repository.PersonRepository
import com.example.weet.repository.PersonRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weet-db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePersonDao(db: AppDatabase): PersonDao {
        return db.personDao()
    }

    @Provides
    @Singleton
    fun providePersonRepository(dao: PersonDao): PersonRepository {
        return PersonRepositoryImpl(dao)
    }

}
