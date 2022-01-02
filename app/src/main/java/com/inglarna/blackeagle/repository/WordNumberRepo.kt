package com.inglarna.blackeagle.repository

import android.content.Context
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.WordNumberDao

class WordNumberRepo(context: Context) {
    private val db = BlackEagleDatabase.getInstance(context)
    private val wordNumberDao: WordNumberDao = db.wordNumberDao()


}