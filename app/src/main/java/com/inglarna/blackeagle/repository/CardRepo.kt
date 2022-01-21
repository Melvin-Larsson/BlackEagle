package com.inglarna.blackeagle.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.room.Query
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.CardDao
import com.inglarna.blackeagle.model.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    fun deleteCards(cards: Set<Card>){
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
    suspend fun getOldCardsByNextRepetition(deckId: Long, cardCount: Int): List<Card> = withContext(Dispatchers.IO){
        cardDao.getOldCardsByNextRepetition(deckId, cardCount)
    }
    suspend fun getNewCardsByNextRepetition(deckId: Long, cardCount: Int): List<Card> = withContext(Dispatchers.IO){
        cardDao.getNewCardsByNextRepetition(deckId, cardCount)
    }
    fun getDeckSize(deckId: Long) : LiveData<Int>{
        return cardDao.loadDeckSize(deckId)
    }
    fun getMaxPosition(deckId: Long): Int{
        return cardDao.getMaxPosition(deckId)
    }
}