package com.inglarna.blackeagle.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) var folderId: Long? = null,
    var name: String = ""
){

}