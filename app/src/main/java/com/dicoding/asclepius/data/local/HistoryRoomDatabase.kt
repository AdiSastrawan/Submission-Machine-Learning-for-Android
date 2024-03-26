package com.dicoding.asclepius.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class HistoryRoomDatabase : RoomDatabase(){
    abstract fun historyDao(): HistoryDao
    companion object{
        @Volatile
        private var instance : HistoryRoomDatabase? = null
        @JvmStatic
        fun getInstance(context : Context) : HistoryRoomDatabase {
            if(instance == null){
                synchronized(HistoryRoomDatabase::class.java){
                    instance = Room.databaseBuilder(context, HistoryRoomDatabase::class.java,"database").build()

                }
            }
            return instance as HistoryRoomDatabase
        }
    }
}