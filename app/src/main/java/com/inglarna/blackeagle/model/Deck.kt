package com.inglarna.blackeagle.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.OutputStreamWriter

@Entity
data class Deck (
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var name: String = "",
    var favorite: Boolean = false
    ){
}