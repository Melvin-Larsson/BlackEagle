package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.WordNumberDao
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.WordNumber

class WordNumberRepo(context: Context) {
    private val db = BlackEagleDatabase.getInstance(context)
    private val wordNumberDao: WordNumberDao = db.wordNumberDao()

    fun getNumbers(): LiveData<List<WordNumber>> {
        return wordNumberDao.loadAll()
    }
    fun getWords(numbers: List<Int>): LiveData<List<WordNumber>>{
        return wordNumberDao.getWords(numbers)
    }
    fun getWords(search: String): LiveData<List<WordNumber>>{
        return wordNumberDao.getWords(search)
    }
    fun updateWord(wordNumber: WordNumber){
        wordNumberDao.updateWord(wordNumber)
    }
    fun updateWords(wordNumbers: List<WordNumber>){
        wordNumberDao.updateWords(wordNumbers)
    }


}