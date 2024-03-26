package com.dicoding.asclepius.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.HistoryDao
import com.dicoding.asclepius.data.local.HistoryEntity
import com.dicoding.asclepius.data.local.HistoryRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppRepository(application : Application) {
    private var mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = HistoryRoomDatabase.getInstance(application)
        mHistoryDao= database.historyDao()
    }
    fun getHistory() : LiveData<List<HistoryEntity>> = mHistoryDao.getAllUser()

    fun insert(history : HistoryEntity){
        executorService.execute{ mHistoryDao.insert(history)}
    }


}