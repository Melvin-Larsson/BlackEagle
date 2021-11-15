package com.inglarna.blackeagle.db

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import com.inglarna.blackeagle.model.Card
import kotlin.math.ln

@Dao
interface CardDao {
    @Query("SELECT * FROM Card Where id =:cardId")
    fun loadCard(cardId: Long): Card

    @Insert(onConflict = IGNORE)
    fun insertCard(card: Card): Long

    @Update(onConflict = REPLACE)
    fun updateCard(card: Card)

    @Delete
    fun deleteCard(card: Card)
}