package com.inglarna.blackeagle.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditDeckViewModelFactory(val application: Application, val deckId: Long): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditDeckViewModel(application, deckId) as T
    }
}