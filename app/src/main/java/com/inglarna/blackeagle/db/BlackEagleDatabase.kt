package com.inglarna.blackeagle.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.WordNumber

@Database(entities = arrayOf(Card::class, Deck::class, WordNumber::class), version = 1)
abstract class BlackEagleDatabase :RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract  fun wordNumberDao(): WordNumberDao

    companion object{
        private var instance: BlackEagleDatabase? = null
        fun getInstance(context: Context): BlackEagleDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,BlackEagleDatabase::class.java,"Database")
                    .createFromAsset("database/numbers.db")
                    .build()
            }
            return instance as BlackEagleDatabase
        }
    }
}