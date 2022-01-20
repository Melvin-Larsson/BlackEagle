package com.inglarna.blackeagle.intervalAlgorithm

import com.inglarna.blackeagle.model.Card
import getCurrentDay
import java.lang.IllegalArgumentException

abstract class AbstractIntervalAlgorithm {
    companion object{
        const val EASY = 1.0
        const val MEDIUM = 0.5
        const val DIFFICULT = 0.0
    }
    fun cardRepeated(card: Card, difficulty: Double, currentDate: Long = getCurrentDay()){
        //Difficulty is outside the allowed interval
        if(difficulty < 0 || difficulty > 1){
            throw IllegalArgumentException("parameter: difficulty must be in the interval 0-1")
        }
        updateCardInterval(card, difficulty, currentDate)
    }
    protected abstract fun updateCardInterval(card: Card, difficulty: Double, currentDate: Long)
}