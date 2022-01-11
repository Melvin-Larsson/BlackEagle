package com.inglarna.blackeagle.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FolderWithDecks (
    @Embedded val folder: Folder,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "deckId",
        associateBy = Junction(FolderDeckCrossRef::class)
    )
    val decks: List<Deck>
){

}