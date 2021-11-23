package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.repository.CardRepo

class CardViewModel (application: Application) : AndroidViewModel(application){
    private var cardRepo: CardRepo = CardRepo(getApplication())

    fun addCard(card: Card){
        cardRepo.addCard(card)
    }

    fun getDeckViews(deckId: Long): LiveData<List<Card>> {
        return cardRepo.getFullDeck(deckId)
    }
}