package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.repository.DeckRepo

class DeckViewModel(application: Application) : AndroidViewModel(application) {
    private var deckRepo: DeckRepo = DeckRepo(getApplication())
    private var decks: LiveData<List<DeckWithCards>>? = null
    private var favoriteDecks: LiveData<List<DeckWithCards>>? = null

    fun addDeck(deck: Deck){
        deckRepo.addDeck(deck)
    }
    fun deleteDeck(deck: Deck){
        deckRepo.deleteDeck(deck)
    }
    fun getDecks(): LiveData<List<DeckWithCards>>? {
        if(decks == null){
            decks = deckRepo.allDecks
        }
        return decks
    }
    fun getFavoriteDecks(): LiveData<List<DeckWithCards>>?{
        if(favoriteDecks == null){
            favoriteDecks = deckRepo.favoriteDecks
        }
        return favoriteDecks
    }
    fun getDeck(id: Long): LiveData<Deck>{
        return deckRepo.getDeck(id)
    }
    fun getDeckWithCards(id: Long): DeckWithCards{
        return deckRepo.getDeckWithCards(id)
    }
    fun getDeckSize(id: Long): Int{
        return deckRepo.getDeckSize(id)
    }
   /*private fun deckToDeckView() {
        decks = Transformations.map(deckRepo.allDecks) { repoDecks ->
            repoDecks.map { deck ->
                deck
            }
        }
    }
    private fun deckToDeckView(deck: Deck) = DeckView(deck.id, deck.favorite, deck.name)

    data class DeckView(
        var id: Long? = null,
        var favorite :  Boolean = false,
        var name : String = ""
    )*/
}