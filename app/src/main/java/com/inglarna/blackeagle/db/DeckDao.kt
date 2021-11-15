package com.inglarna.blackeagle.db

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards

@Dao
interface DeckDao {
    @Transaction
    @Query("SELECT * FROM Deck WHERE id=:id")
    fun getDeck(id: Long) : Deck

    @Transaction
    @Query("SELECT * From Deck")
    fun getDecks(): List<DeckWithCards>

    @Insert(onConflict = IGNORE)
    fun insertDeck(deck: Deck) : Long?

    @Update(onConflict = REPLACE)
    fun updateDeck(deck: Deck)


}