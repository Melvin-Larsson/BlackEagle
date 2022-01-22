package com.inglarna.blackeagle.ui.deckPicker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.repository.FolderRepo

class DeckPickerViewModel(application: Application) : AndroidViewModel(application) {
    private val folderRepo = FolderRepo(application)

    private val excludedFolderId = MutableLiveData<Long>(-1)

    private var selectedDecksSet = mutableSetOf<Deck>()
    private val _selectedDecks: MutableLiveData<Set<Deck>> = MutableLiveData(selectedDecksSet)
    val selectedDecks: LiveData<Set<Deck>>
        get() = _selectedDecks

    val decks = excludedFolderId.switchMap { excludedFolderId ->
        Log.d("DeckPicker", "id: $excludedFolderId")
        folderRepo.getDecksOutsideOfFolder(excludedFolderId)
    }

    fun setExcludedFolder(folderId: Long){
        excludedFolderId.value = folderId
    }

    /**
     * Toggles the selection status of the deck if the selection mode is on (select = true).
     * If the deck is selected it becomes unselected and if the deck is not selected it
     * becomes selected.
     */
    fun setSelectionState(deck: Deck, selected: Boolean){
        if(selected){
            selectedDecksSet.add(deck)
        }else{
            selectedDecksSet.remove(deck)
        }
        _selectedDecks.value = selectedDecksSet
    }
}