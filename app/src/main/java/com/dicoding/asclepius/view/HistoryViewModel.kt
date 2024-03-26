package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.HistoryEntity
import com.dicoding.asclepius.data.AppRepository

class HistoryViewModel (application: Application): ViewModel( ) {
    private val repository : AppRepository = AppRepository(application)

    fun getHistory() : LiveData<List<HistoryEntity>> = repository.getHistory()

}