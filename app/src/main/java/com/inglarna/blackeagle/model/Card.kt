package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.math.ln

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var deckId: Long? = null,
    var question : String = "",
    var answer : String = "",
    var hint : String = "",
    var position : Int = 0,
    var firstRepetition : Double? = null,
    var nextRepetition : Double = millisToDays(Date().time).toDouble(),
    var k : Double = 1.0,
){
    companion object{
        private const val TAG = "Card"
        private const val NEXT_REPETITION_RETRIEVABILITY  = 0.7
        private val rand = Random()
        private fun millisToDays(millis : Long) : Long{
            return millis / (1000 * 3600 * 24)
        }
    }

    fun repeated(retrieveability: Double){
        if(retrieveability <= 0){
            firstRepetition = null
            nextRepetition = millisToDays(Date().time) + rand.nextDouble()
            k = 1.0
        }else{
            if(firstRepetition == null){
                firstRepetition = millisToDays(Date().time).toDouble()
                nextRepetition = firstRepetition!! + 1
            }else{
                k *= NEXT_REPETITION_RETRIEVABILITY
                var t = ln(k/0.8584) / -0.0881
                nextRepetition = t.toLong() + firstRepetition!!
            }
        }
    }
}
