package com.inglarna.blackeagle.ui.cardlist

import android.app.Application
import android.net.Uri
import androidx.arch.core.util.Function
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.repository.DeckRepo
import com.inglarna.blackeagle.repository.FolderRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditDeckViewModel(application: Application, val deckId: Long): AndroidViewModel(application) {
    private val deckRepo = DeckRepo(getApplication())
    private val folderRepo = FolderRepo(getApplication())

    val deck: LiveData<Deck> by lazy {
        deckRepo.getLiveDeck(deckId)
    }
    val deckWithCards: LiveData<DeckWithCards> by lazy {
        deckRepo.getLiveDeckWithCards(deckId)
    }

    fun deleteDeck(){
        GlobalScope.launch {
            val deck = deckRepo.getDeck(deckId)
            deckRepo.deleteDeck(deck)
        }
    }

    fun updateDeck(deckUpdater: ((Deck) -> Unit)){
        GlobalScope.launch {
            val deck = deckRepo.getDeck(deckId)
            deckUpdater(deck)
            deckRepo.updateDeck(deck)
        }
    }

    fun addDeckToFolder(folderId: Long){
        GlobalScope.launch {
            folderRepo.addDeckToFolder(deckId, folderId)
        }
    }

    fun export(destination: Uri){
        GlobalScope.launch {
            val deck = deckRepo.getDeckWithCards(deckId)
            deck.export(getApplication(), destination)
        }
    }
}