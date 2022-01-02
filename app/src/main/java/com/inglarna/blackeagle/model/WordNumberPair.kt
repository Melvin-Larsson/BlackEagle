package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordNumberPair (
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var word: String,
    var number: Int
){
}
