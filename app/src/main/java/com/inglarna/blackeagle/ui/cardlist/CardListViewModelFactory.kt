package com.inglarna.blackeagle.ui.cardlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CardListViewModelFactory(val application: Application, val deckId: Long): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardListViewModel(application, deckId) as T
    }
}