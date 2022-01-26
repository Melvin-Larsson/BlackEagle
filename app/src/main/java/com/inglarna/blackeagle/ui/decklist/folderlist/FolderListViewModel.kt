package com.inglarna.blackeagle.ui.decklist.folderlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.model.FolderWithDecks
import com.inglarna.blackeagle.repository.FolderRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FolderListViewModel (application: Application): AndroidViewModel(application){
    private val folderRepo = FolderRepo(getApplication())
    private val _select = MutableLiveData(false)
    val select: LiveData<Boolean>
        get() = _select
    private var selectedFoldersSet = mutableSetOf<Folder>()
    val folders = folderRepo.getFolders()
    private val _selectedFolders: MutableLiveData<Set<Folder>> = MutableLiveData(selectedFoldersSet)
    val selectedFolders: LiveData<Set<Folder>> //TODO: write about this maybe?
        get() = _selectedFolders

    fun addFolder(folderName: String){
        GlobalScope.launch {
            val folder = Folder()
            folder.name = folderName
            folderRepo.addFolder(folder)
        }
    }
    fun removeSelectedFolders(){
        _select.value = false
        GlobalScope.launch { //FIXME: should Globalscope be used here?
            for (folder in selectedFoldersSet){
                folderRepo.deleteFolder(folder)
            }
            selectedFoldersSet.clear()
        }
    }
    fun onFolderLongClicked(folder: Folder): Boolean{
        setSelect(true)
        setSelectionState(folder, true)
        return true //if method is called in onLongClick (in list_item_folder.xml for example), a boolean return value is required
    }

    fun setSelect(select: Boolean){
        _select.value = select
        selectedFoldersSet.clear()
    }
    /**
     * Toggles the selection status of the deck if the selection mode is on (select = true).
     * If the deck is selected it becomes unselected and if the deck is not selected it
     * becomes selected.
     */
    fun setSelectionState(folder: Folder, selected: Boolean){
        if(select.value!!){
            if(selected){
                selectedFoldersSet.add(folder)
            }else{
                selectedFoldersSet.remove(folder)
            }
            _selectedFolders.value = selectedFoldersSet
        }
    }
    fun toggleSelectAll(){
        selectedFoldersSet = if(selectedFoldersSet.size == folders.value!!.size){
            mutableSetOf() //Deselect all
        }else{
            folders.value!!.toMutableSet() //Select all
        }
        _selectedFolders.value = selectedFoldersSet
    }
}