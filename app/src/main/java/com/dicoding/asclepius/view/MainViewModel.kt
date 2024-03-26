package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.HistoryEntity
import com.dicoding.asclepius.data.HistoryRepository

class MainViewModel(application: Application) : ViewModel() {
    private val repository : HistoryRepository = HistoryRepository(application)

    fun insert(historyEntity: HistoryEntity){
        repository.insert(historyEntity)
    }
}