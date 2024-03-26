package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.HistoryEntity
import com.dicoding.asclepius.data.AppRepository

class MainViewModel(application: Application) : ViewModel() {
    private val repository : AppRepository = AppRepository(application)

    fun insert(historyEntity: HistoryEntity){
        repository.insert(historyEntity)
    }
}