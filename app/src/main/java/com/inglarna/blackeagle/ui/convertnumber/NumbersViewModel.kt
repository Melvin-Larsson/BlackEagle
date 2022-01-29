package com.inglarna.blackeagle.ui.convertnumber

import android.app.Application
import androidx.lifecycle.*
import com.inglarna.blackeagle.repository.WordNumberRepo

class NumbersViewModel(application: Application) : AndroidViewModel(application){
    private val wordNumberRepo = WordNumberRepo(application)

    val number = MutableLiveData("")
    val words: LiveData<String> = number.switchMap { number ->
        val numbers = divideNumber(number)
        wordNumberRepo.getWords(numbers).map { wordNumbers ->
            var result = ""
            for (number in numbers) {
                for (wordNumber in wordNumbers) {
                    if (wordNumber.number == number) {
                        result += wordNumber.word + " "
                        break
                    }
                }
            }
            result
        }


    }
    private fun divideNumber(number: String): List<Int>{
        val numbers = mutableListOf<Int>()
        var startIndex = 0
        if(number.length%3 != 0){
            numbers.add(number.substring(startIndex, startIndex + number.length%3).toInt())
            startIndex += number.length%3
        }
        while(startIndex < number.length){
            numbers.add(number.substring(startIndex, startIndex + 3).toInt())
            startIndex += 3;
        }
        return numbers
    }
}