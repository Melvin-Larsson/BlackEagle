package com.inglarna.blackeagle.ui.folder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FolderViewModelFactory(val application: Application, val folderId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FolderViewModel(application, folderId) as T
    }
}