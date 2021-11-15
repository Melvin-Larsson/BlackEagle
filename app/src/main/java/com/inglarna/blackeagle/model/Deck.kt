package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Deck (
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var name: String = "",
    ){
}