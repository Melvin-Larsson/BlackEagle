package com.inglarna.blackeagle.ui.decklist.decklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.repository.FolderRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeckListViewModel(application: Application): AndroidViewModel(application)  {
    private val folderRepo = FolderRepo(application)

    fun addFolder(folderName: String){
        GlobalScope.launch {
            val folder = Folder()
            folder.name = folderName
            folderRepo.addFolder(folder)
        }
    }
}