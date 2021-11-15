package com.inglarna.blackeagle.repository

import android.content.Context
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.CardDao
import com.inglarna.blackeagle.db.DeckDao
import com.inglarna.blackeagle.model.Deck

class DeckRepo(context: Context) {
    private val db = BlackEagleDatabase.getInstance(context)
    private val deckDao: DeckDao = db.deckDao()
}