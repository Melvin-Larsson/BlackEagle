package com.inglarna.blackeagle.ui.decklist.folderlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.model.FolderWithDecks
import com.inglarna.blackeagle.repository.FolderRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FolderListViewModel (application: Application): AndroidViewModel(application){
    private val folderRepo = FolderRepo(getApplication())

    val folderWithDecks: LiveData<List<FolderWithDecks>> = folderRepo.getAllFoldersWithDecks()

    fun addFolder(folderName: String){
        GlobalScope.launch {
            val folder = Folder()
            folder.name = folderName
            folderRepo.addFolder(folder)
        }
    }
}