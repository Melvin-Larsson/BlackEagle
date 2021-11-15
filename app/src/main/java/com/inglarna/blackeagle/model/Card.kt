package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.ln

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var deckId: Long? = null,
    var question : String = "",
    var answer : String = "",
    var hint : String = "",
    var position : Int = 0,
    var repititions: Int = 0){


    fun nextRepitition() : Double{
        var t = 0.0
        val R = 0.7
        t = ln(R) * -repititions
        return t
    }
}