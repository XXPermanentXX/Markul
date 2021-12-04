package com.markul.android.logic.room

import android.content.Context
import androidx.core.content.contentValuesOf
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.markul.android.MarkulApplication

@Database(entities = [User::class,Album::class,Photo::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao(): AppDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
