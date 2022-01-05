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
    fun updateCard(card: Card){
        cardDao.updateCard(card)
    }
    fun updateCards(cards: Set<Card>){
        cardDao.updateCards(cards)
    }
    fun getFullDeck(deckId: Long): LiveData<List<Card>>{
        return cardDao.loadFullDeck(deckId)
    }
    fun getFullDeckByNextRepetition(deckId: Long, maxDay: Double): LiveData<List<Card>>{
        return cardDao.loadFullDeckByNextRepetition(deckId, maxDay)
    }
    fun getDeckSize(deckId: Long) : LiveData<Int>{
        return cardDao.loadDeckSize(deckId)
    }
    fun getMaxPosition(deckId: Long): Int{
        return cardDao.getMaxPosition(deckId)
    }
}