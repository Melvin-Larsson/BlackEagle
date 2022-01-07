package com.inglarna.blackeagle.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import com.inglarna.blackeagle.model.Card
import kotlin.math.ln

@Dao
interface CardDao {

    @Query("SELECT * FROM Card Where id = :cardId")
    fun loadCard(cardId: Long): LiveData<Card>

    @Query("SELECT * FROM Card WHERE deckId = :deckId ORDER BY position ASC")
    fun loadFullDeck(deckId: Long): LiveData<List<Card>>

    @Query("SELECT * FROM Card WHERE deckId = :deckId AND nextRepetition < :maxDay ORDER BY nextRepetition ASC")
    fun loadFullDeckByNextRepetition(deckId: Long, maxDay :Double): LiveData<List<Card>>

    @Query("SELECT COUNT(deckId) FROM Card WHERE deckId = :deckId")
    fun loadDeckSize(deckId: Long): LiveData<Int>

    @Query("SELECT MAX(position) FROM Card WHERE deckId = :deckId")
    fun getMaxPosition(deckId: Long) : Int

    @Insert(onConflict = IGNORE)
    fun insertCard(card: Card): Long

    @Insert(onConflict = IGNORE)
    fun insertCards(cards: List<Card>): List<Long>

    @Update(onConflict = REPLACE)
    fun updateCard(card: Card)

    @Update(onConflict = REPLACE)
    fun updateCards(cards: Set<Card>)

    @Delete
    fun deleteCard(card: Card)

    @Delete
    fun deleteCards(cards: List<Card>)
}