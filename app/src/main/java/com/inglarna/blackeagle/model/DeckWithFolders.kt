package com.inglarna.blackeagle.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DeckWithFolders(
    @Embedded val deck: Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "folderId",
        associateBy = Junction(FolderDeckCrossRef::class)
    )
    val folders: List<Folder>
){

}