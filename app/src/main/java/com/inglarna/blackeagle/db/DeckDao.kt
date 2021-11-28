package com.inglarna.blackeagle.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards

@Dao
interface DeckDao {
    @Query ("SELECT * FROM Deck")
    fun loadAll(): LiveData<List<Deck>>

    @Transaction
    @Query("SELECT * FROM Deck WHERE id=:id")
    fun getDeck(id: Long) : Deck

    @Query("SELECT * FROM Deck WHERE favorite=1")
    fun getFavouriteDecks(): LiveData<List<Deck>>

    @Transaction
    @Query ("SELECT * FROM Deck WHERE id= :id")
    fun loadLiveDeck(id: Long) : LiveData<Deck>

    @Transaction
    @Query("SELECT * From Deck")
    fun getDecks(): List<DeckWithCards>

    @Insert(onConflict = IGNORE)
    fun insertDeck(deck: Deck) : Long?

    @Update(onConflict = REPLACE)
    fun updateDeck(deck: Deck)


}