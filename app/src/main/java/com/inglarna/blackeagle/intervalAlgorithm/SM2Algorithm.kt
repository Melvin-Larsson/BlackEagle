/* TODO: Make this correct (https://supermemopedia.com/wiki/Licensing_SuperMemo_Algorithm):
    Algorithm SM-2, (C) Copyright SuperMemo World, 1991.

    https://www.supermemo.com
    https://www.supermemo.eu
 */

package com.inglarna.blackeagle.intervalAlgorithm

import com.inglarna.blackeagle.model.Card
import java.util.*
import kotlin.math.ceil
import kotlin.math.max

class SM2Algorithm : AbstractIntervalAlgorithm() {
    companion object{
        private val rand = Random()
    }

    override fun updateCardInterval(card: Card, difficulty: Double, currentDate: Long) {
        val grade = difficulty * 5 //convert difficulty to a scale 0-5

        //If the card is being repeated early multiple times the same day, the interval should not increase, only decrease if grade < 2
        if(grade >= 2 && card.lastRepetition != null && currentDate == card.lastRepetition!!.toLong() && currentDate != card.nextRepetition.toLong()){
            return
        }

        //Calculate easiness factor (EF)
        val newEasinessFactor = card.easinessFactor + (0.1-(5-grade)*(0.08+(5-grade)*0.02))
        card.easinessFactor = max(newEasinessFactor, 1.3)

        //calculate next repetition
        card.repetitions++
        if(grade < 2){
            card.repetitions = 0
        }
        val interval = when(card.repetitions){
            0 -> rand.nextDouble()
            1 -> 1.0
            2 -> 6.0
            else -> {
                val lastInterval = currentDate - card.lastRepetition!!
                ceil(lastInterval * card.easinessFactor)
            }
        }
        card.nextRepetition = currentDate + interval
        card.lastRepetition = currentDate.toDouble()
    }
}