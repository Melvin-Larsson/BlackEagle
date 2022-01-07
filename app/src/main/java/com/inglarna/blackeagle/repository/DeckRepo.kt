package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.PictureUtils
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.CardDao
import com.inglarna.blackeagle.db.DeckDao
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import java.io.File

class DeckRepo(val context: Context) {
    private val db = BlackEagleDatabase.getInstance(context)
    private val deckDao: DeckDao = db.deckDao()
    private val cardDao: CardDao = db.cardDao()

    fun addDeck(deck: Deck): Long{
        val newId = deckDao.insertDeck(deck)
        deck.id = newId
        return newId
    }
    fun getDeck(id: Long): LiveData<Deck>{
        return deckDao.loadLiveDeck(id)
    }
    fun getDeckWithCards(id: Long): DeckWithCards{
        return  deckDao.getDeckWithCards(id)
    }
    fun getDeckSize(id: Long): Int{
        return deckDao.getDeckSize(id)
    }

    fun deleteDeck(deck: Deck) {
        val deckWithCards = deckDao.getDeckWithCards(deck.id!!)
        for(card in deckWithCards.cards){
            val imageFiles = PictureUtils.getImageFilesFromId(context.filesDir, card.id!!)
            for(imageFile in imageFiles){
                imageFile.delete()
            }
        }
        deckDao.deleteDeck(deck)
        cardDao.deleteCards(deckWithCards.cards)
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