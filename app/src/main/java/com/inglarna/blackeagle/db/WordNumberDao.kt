package com.inglarna.blackeagle.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.WordNumber

@Dao
interface WordNumberDao {

    @Query("SELECT * FROM WordNumber")
    fun loadAll(): LiveData<List<WordNumber>>

    @Query("SELECT * FROM WordNumber WHERE id = :wordNumberId")
    fun loadFullDeck(wordNumberId: Long): LiveData<List<WordNumber>>
}