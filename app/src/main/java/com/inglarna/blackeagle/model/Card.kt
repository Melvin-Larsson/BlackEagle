package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var deckId: Long? = null,
    var question : String = "",
    var answer : String = "",
    var hint : String = "",
    var position : Int = 0) {
}