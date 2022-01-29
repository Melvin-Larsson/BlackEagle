package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import daysToMillis
import getCurrentDay
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Stat(
    @PrimaryKey var date: Long? = getCurrentDay(),
    var studies: Long = 0
){
    fun getFormattedDate(): String { //FIXME: old
        val formatter = SimpleDateFormat("MM-dd")
        val date = Date(daysToMillis(date!!))
        return formatter.format(date)
    }
}
