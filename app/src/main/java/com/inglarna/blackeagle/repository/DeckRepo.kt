package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.CardDao
import com.inglarna.blackeagle.db.DeckDao
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards

class DeckRepo(context: Context) {
    private val db = BlackEagleDatabase.getInstance(context)
    private val deckDao: DeckDao = db.deckDao()

    fun addDeck(deck: Deck): Long?{
        val newId = deckDao.insertDeck(deck)
        deck.id = newId
        return newId
    }
    fun getDeck(id: Long) : LiveData<Deck>{
        return deckDao.loadLiveDeck(id)
    }

    fun deleteDeck(deck: Deck) {
        deckDao.deleteDeck(deck)
    }

    val favoriteDecks: LiveData<List<DeckWithCards>>
        get(){
            return deckDao.getFavoriteDecks()
        }

    val allDecks: LiveData<List<DeckWithCards>>
        get(){
            return deckDao.getDecks()
        }
}