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

    @Query("SELECT * FROM WordNumber WHERE number IN (:numbers)")
    fun getWords(numbers: List<Int>): LiveData<List<WordNumber>>

    @Query("SELECT * FROM WordNumber WHERE word LIKE '%' || :search || '%' OR number LIKE :search || '%'")
    fun getWords(search: String): LiveData<List<WordNumber>>
}