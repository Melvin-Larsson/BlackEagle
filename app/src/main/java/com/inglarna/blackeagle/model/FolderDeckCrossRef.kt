package com.inglarna.blackeagle.model

import androidx.room.Entity

@Entity(primaryKeys = ["folderId", "deckId"])
data class FolderDeckCrossRef (
    val folderId: Long,
    val deckId: Long
){
}