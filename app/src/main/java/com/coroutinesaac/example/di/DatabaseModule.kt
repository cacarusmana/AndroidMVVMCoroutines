package com.coroutinesaac.example.di

import android.arch.persistence.room.Room
import android.content.Context
import com.coroutinesaac.example.database.AppDatabase
import com.coroutinesaac.example.database.ProductDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase): ProductDao {
        return db.productDao()
    }
}