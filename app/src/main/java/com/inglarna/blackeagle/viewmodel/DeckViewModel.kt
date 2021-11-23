package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.repository.DeckRepo

class DeckViewModel(application: Application) : AndroidViewModel(application) {
    private var deckRepo: DeckRepo = DeckRepo(getApplication())
    private var decks: LiveData<List<DeckView>>? = null

    fun addDeck(deck: Deck){
        deckRepo.addDeck(deck)
    }

    fun getDeckViews(): LiveData<List<DeckView>>? {
        if(decks == null){
            deckToDeckView()
        }
        return decks
    }
   private fun deckToDeckView() {
        decks = Transformations.map(deckRepo.allDecks) { repoDecks ->
            repoDecks.map { deck ->
                deckToDeckView(deck)
            }
        }
    }
    private fun deckToDeckView(deck: Deck) = DeckView(deck.id, deck.favorite, deck.name)

    data class DeckView(
        var id: Long? = null,
        var favorite :  Boolean = false,
        var name : String = ""
    )
}