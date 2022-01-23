package com.inglarna.blackeagle.ui.folder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditFolderViewModelFactory(private val application: Application, private val folderId: Long): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditFolderViewModel(application, folderId) as T
    }
}