package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.model.FolderWithDecks
import com.inglarna.blackeagle.repository.FolderRepo

class FolderListViewModel (application: Application): AndroidViewModel(application){
    private val folderRepo = FolderRepo(getApplication())

    val folderWithDecks: LiveData<List<FolderWithDecks>> by lazy{
        folderRepo.getAllFoldersWithDecks()
    }
    fun addFolder(folder: Folder){
        folderRepo.addFolder(folder)
    }
}