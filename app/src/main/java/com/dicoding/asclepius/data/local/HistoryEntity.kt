package com.dicoding.asclepius.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName="history")
data class HistoryEntity (
    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    @ColumnInfo(name="image")
    var image : String,
    @ColumnInfo(name="result")
    var result : String,
    @ColumnInfo(name="score")
    var score : String
)