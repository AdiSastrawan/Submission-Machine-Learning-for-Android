package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(historyEntity: HistoryEntity)
    @Query("select * from history")
    fun getAllUser() : LiveData<List<HistoryEntity>>
}