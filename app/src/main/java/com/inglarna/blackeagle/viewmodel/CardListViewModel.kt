package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.repository.CardRepo
import com.inglarna.blackeagle.repository.DeckRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CardListViewModel (application: Application, val deckId: Long): AndroidViewModel(application){
    private var cardRepo: CardRepo = CardRepo(getApplication())
    private var deckRepo: DeckRepo = DeckRepo(getApplication())

    val cards: LiveData<List<Card>> by lazy{
        cardRepo.getFullDeck(deckId)
    }
    val deck: LiveData<Deck> by lazy {
        deckRepo.getLiveDeck(deckId)
    }
    fun getCardsByNextRepetition(maxDay: Double): LiveData<List<Card>>{
        return cardRepo.getFullDeckByNextRepetition(deckId, maxDay)
    }
    fun deleteCard(card: Card){
        GlobalScope.launch {
            cardRepo.deleteCard(card)
        }
    }
    fun deleteCards(cards: List<Card>){
        GlobalScope.launch {
            cardRepo.deleteCards(cards)
        }
    }
    fun updateCards(cards: Set<Card>){
        cardRepo.updateCards(cards)
    }

}