package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.WordNumber
import com.inglarna.blackeagle.repository.WordNumberRepo

class wordNumberViewModel(application: Application) : AndroidViewModel(application) {
    private var wordNumberRepo: WordNumberRepo = WordNumberRepo(getApplication())
    var id : Long = -1

    fun getNumberViews(): LiveData<List<WordNumber>> {
        return wordNumberRepo.getNumbers()
    }
    fun getWords(numbers: List<Int>): LiveData<List<WordNumber>>{
        return wordNumberRepo.getWords(numbers)
    }
    fun getWords(search: String): LiveData<List<WordNumber>>{
        return wordNumberRepo.getWords(search)
    }
    fun updateWord(wordNumber: WordNumber){
        wordNumberRepo.updateWord(wordNumber)
    }
    fun updateWords(wordNumbers: MutableSet<WordNumber>){
        wordNumberRepo.updateWords(wordNumbers)
    }
}