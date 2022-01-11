package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.PictureUtils
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.CardDao
import com.inglarna.blackeagle.db.DeckDao
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards

class DeckRepo(val context: Context) {
    private val db = BlackEagleDatabase.getInstance(context)
    private val deckDao: DeckDao = db.deckDao()
    private val cardDao: CardDao = db.cardDao()

    fun addDeck(deck: Deck): Long{
        val newId = deckDao.insertDeck(deck)
        deck.deckId = newId
        return newId
    }
    fun getDeck(id: Long): Deck{
        return deckDao.getDeck(id)
    }
    fun getLiveDeck(id: Long): LiveData<Deck>{
        return deckDao.loadLiveDeck(id)

    }
    fun getDecks(): LiveData<List<Deck>>{
        return deckDao.loadAll()
    }
    fun getDeckWithCards(id: Long): DeckWithCards{
        return  deckDao.getDeckWithCards(id)
    }
    fun getLiveDeckWithCards(id: Long): LiveData<DeckWithCards>{
        return deckDao.getLiveDeckWithCards(id)
    }
    fun getDeckSize(id: Long): Int{
        return deckDao.getDeckSize(id)
    }

    fun deleteDeck(deck: Deck) {
        val deckWithCards = deckDao.getDeckWithCards(deck.deckId!!)
        for(card in deckWithCards.cards){
            val imageFiles = PictureUtils.getImageFilesFromId(context.filesDir, card.cardId!!)
            for(imageFile in imageFiles){
                imageFile.delete()
            }
        }
        deckDao.deleteDeck(deck)
        cardDao.deleteCards(deckWithCards.cards)
    }
    fun deleteDecks(decks: List<Deck>){
        for(deck in decks){
            deleteDeck(deck)
        }
    }
    fun updateDeck(deck: Deck){
        deckDao.updateDeck(deck)
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