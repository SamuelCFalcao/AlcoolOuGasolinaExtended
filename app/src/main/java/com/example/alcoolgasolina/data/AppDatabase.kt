package com.example.alcoolgasolina.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Posto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postoDao(): PostoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "postos_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
