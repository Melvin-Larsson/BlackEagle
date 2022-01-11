package com.inglarna.blackeagle.viewmodel

import android.app.Application
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.repository.DeckRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeckListViewModel(application: Application): AndroidViewModel(application){
    private var deckRepo = DeckRepo(getApplication())
    val decks: LiveData<List<DeckWithCards>> by lazy {
        deckRepo.allDecks
    }
    val favoriteDecks: LiveData<List<DeckWithCards>> by lazy {
        deckRepo.favoriteDecks
    }
    fun deleteDeck(deck: Deck){
        GlobalScope.launch {
            deckRepo.deleteDeck(deck)
        }
    }
    fun deleteDecks(decks: List<Deck>){
        GlobalScope.launch {
            deckRepo.deleteDecks(decks)
        }
    }
}