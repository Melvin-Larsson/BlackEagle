package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.repository.CardRepo

class CardViewModel (application: Application) : AndroidViewModel(application){
    private var cardRepo: CardRepo = CardRepo(getApplication())
    var id : Long = -1

    fun addCard(card: Card){
        cardRepo.addCard(card)
    }
    fun deleteCard(card: Card){
        cardRepo.deleteCard(card)
    }
    fun getCard(cardId: Long): LiveData<Card>{
        return cardRepo.getCard(cardId)
    }
    fun getDeckViews(deckId: Long): LiveData<List<Card>> {
        return cardRepo.getFullDeck(deckId)
    }
    fun getDeckByNextRepetition(deckId: Long, maxDay: Double):LiveData<List<Card>>{
        return cardRepo.getFullDeckByNextRepetition(deckId, maxDay)
    }
    fun updateCard(card: Card){
        cardRepo.updateCard(card)
    }
    fun updateCards(cards: Set<Card>){
        cardRepo.updateCards(cards)
    }
    fun getMaxPosition(deckId: Long): Int{
        return cardRepo.getMaxPosition(deckId)
    }
}