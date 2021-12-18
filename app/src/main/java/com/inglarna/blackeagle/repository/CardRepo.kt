package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.CardDao
import com.inglarna.blackeagle.model.Card

class CardRepo (context: Context){
    private val db = BlackEagleDatabase.getInstance(context)
    private val cardDao: CardDao = db.cardDao()

    fun addCard(card: Card): Long?{
        val newId = cardDao.insertCard(card)
        card.id = newId
        return newId
    }
    fun deleteCard(card: Card){
        cardDao.deleteCard(card)
    }
    fun getFullDeck(deckId: Long): LiveData<List<Card>>{
        return cardDao.loadFullDeck(deckId)
    }
    fun getDeckSize(deckId: Long) : LiveData<Int>{
        return cardDao.loadDeckSize(deckId)
    }
}