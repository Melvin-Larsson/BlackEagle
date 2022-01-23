package com.inglarna.blackeagle.ui.folder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.repository.FolderRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FolderViewModel(application: Application, val folderId: Long): AndroidViewModel(application) {
    private val folderRepo = FolderRepo(application)

    private val _select = MutableLiveData(false)
    val select: LiveData<Boolean>
        get() = _select

    val folderWithDeck = folderRepo.getFolderWithDecks(folderId)

    //TODO: This correct? correct solution?
    //Livedata observers are only notified when the livedata gets a new value, i.e.
    //when livedata.setValue() is called but not when the contents of
    //the list are changed. To solve this the list is stored separately and put into
    //the livedata (setValue()) everytime the list contents change
    //https://stackoverflow.com/questions/47941537/notify-observer-when-item-is-added-to-list-of-livedata
    private var selectedDecksSet = mutableSetOf<Deck>()
    private val _selectedDecks: MutableLiveData<Set<Deck>> = MutableLiveData(selectedDecksSet)
    val selectedDecks: LiveData<Set<Deck>> //TODO: write about this maybe?
        get() = _selectedDecks

    fun addDecks(deckIds: LongArray){
        GlobalScope.launch {
            for(deckId in deckIds){
                folderRepo.addDeckToFolder(deckId, folderId)
            }
        }
    }

    fun removeSelectedDecks(){
        _select.value = false
        GlobalScope.launch { //FIXME: should Globalscope be used here?
            for (deck in selectedDecksSet){
                folderRepo.removeDeckFromFolder(folderId, deck.deckId!!)
            }
            selectedDecksSet.clear()
        }
    }

    fun onDeckLongClicked(deck: Deck): Boolean{
        setSelect(true)
        setSelectionState(deck, true)
        return true //if method is called in onLongClick (in list_item_folder.xml for example), a boolean return value is required
    }

    fun setSelect(select: Boolean){
        _select.value = select
        selectedDecksSet.clear()
    }
    fun toggleSelectAll(){
        selectedDecksSet = if(selectedDecksSet.size == folderWithDeck.value!!.decks.size){
            mutableSetOf() //Deselect all
        }else{
            folderWithDeck.value!!.decks.toMutableSet() //Select all
        }
        _selectedDecks.value = selectedDecksSet
    }

    /**
     * Toggles the selection status of the deck if the selection mode is on (select = true).
     * If the deck is selected it becomes unselected and if the deck is not selected it
     * becomes selected.
     */
    fun setSelectionState(deck: Deck, selected: Boolean){
        if(select.value!!){
            if(selected){
                selectedDecksSet.add(deck)
            }else{
                selectedDecksSet.remove(deck)
            }
            _selectedDecks.value = selectedDecksSet
        }
    }
}