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
    @Query("SELECT * FROM Deck WHERE deckId=:id")
    fun getDeck(id: Long) : Deck

    @Transaction
    @Query("SELECT * FROM Deck WHERE deckId=:id")
    fun getDeckWithCards(id:Long): DeckWithCards

    @Transaction
    @Query("SELECT * FROM Deck WHERE deckId=:id")
    fun getLiveDeckWithCards(id: Long): LiveData<DeckWithCards>


    @Transaction
    @Query ("SELECT * FROM Deck WHERE deckId= :id")
    fun loadLiveDeck(id: Long) : LiveData<Deck>

    @Transaction
    @Query("SELECT * FROM Deck")
    fun getDecks(): LiveData<List<DeckWithCards>>

    @Transaction
    @Query("SELECT * FROM Deck WHERE favorite=1")
    fun getFavoriteDecks(): LiveData<List<DeckWithCards>>

    @Query("SELECT COUNT(cardId) FROM Card WHERE deckId=:id")
    fun getDeckSize(id: Long): Int

    @Insert(onConflict = IGNORE)
    fun insertDeck(deck: Deck) : Long

    @Update(onConflict = REPLACE)
    fun updateDeck(deck: Deck)

    @Delete
    fun deleteDeck(deck: Deck)


}