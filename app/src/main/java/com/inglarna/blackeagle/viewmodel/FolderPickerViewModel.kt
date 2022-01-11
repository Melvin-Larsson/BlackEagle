package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.repository.FolderRepo

class FolderPickerViewModel(application: Application): AndroidViewModel(application) {
    private val folderRepo = FolderRepo(getApplication())

    val folders: LiveData<List<Folder>> by lazy {
        folderRepo.getFolders()
    }
}