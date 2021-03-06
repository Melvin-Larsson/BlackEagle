package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Deck (
    @PrimaryKey(autoGenerate = true) var deckId:Long? = null,
    var name: String = "",
    var favorite: Boolean = false
){
}