package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.CardDao
import com.inglarna.blackeagle.model.Card

class CardRepo (context: Context){
    private val db = BlackEagleDatabase.getInstance(context)
    private val cardDao: CardDao = db.cardDao()

    fun addCard(card: Card): Long{
        val newId = cardDao.insertCard(card)
        card.cardId = newId
        return newId
    }
    fun addCards(cards: List<Card>): List<Long>{
        return cardDao.insertCards(cards)
    }
    fun deleteCard(card: Card){
        cardDao.deleteCard(card)
    }
    fun deleteCards(cards: List<Card>){
        cardDao.deleteCards(cards)
    }
    fun updateCard(card: Card){
        cardDao.updateCard(card)
    }
    fun updateCards(cards: Set<Card>){
        cardDao.updateCards(cards)
    }
    fun getCard(cardId: Long): LiveData<Card>{
        return cardDao.loadCard(cardId)
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