package com.dicoding.asclepius.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.HistoryEntity
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.HistoryRoomDatabase

class HistoryViewModel (application: Application): ViewModel( ) {
    private val repository : HistoryRepository = HistoryRepository(application)

    fun getHistory() : LiveData<List<HistoryEntity>> = repository.getHistory()

}