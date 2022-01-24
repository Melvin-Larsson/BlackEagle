package com.inglarna.blackeagle.ui.card

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CardViewModelFactory(val application: Application, val deckId: Long, val cardId: Long = -1): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardViewModel(application, deckId, cardId) as T
    }
}