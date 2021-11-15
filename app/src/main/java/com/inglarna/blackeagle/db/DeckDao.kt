package com.inglarna.blackeagle.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards

@Dao
interface DeckDao {
    @Transaction
    @Query("SELECT * FROM Deck WHERE id=:id")
    fun getDeck(id: Deck) : Deck

    @Transaction
    @Query("SELECT * From Deck")
    fun getDecks(): List<DeckWithCards>

    @Insert(onConflict = IGNORE)
    fun insertDeck(deck: Deck) : Long?
}