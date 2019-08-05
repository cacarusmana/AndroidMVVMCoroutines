package com.coroutinesaac.example.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.coroutinesaac.example.model.Converters
import com.coroutinesaac.example.model.TProduct

@Database(entities = [TProduct::class], version = AppDatabase.DB_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        const val DB_NAME = "alfamet.db"
        const val DB_VERSION = 1
    }
}