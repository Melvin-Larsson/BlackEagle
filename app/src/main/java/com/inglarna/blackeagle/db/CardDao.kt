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
    fun loadCard(cardId: Long): Card

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    fun loadFullDeck(deckId: Long): LiveData<List<Card>>

    @Insert(onConflict = IGNORE)
    fun insertCard(card: Card): Long

    @Update(onConflict = REPLACE)
    fun updateCard(card: Card)

    @Delete
    fun deleteCard(card: Card)
}