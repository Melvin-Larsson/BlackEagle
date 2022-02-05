package com.inglarna.blackeagle.db

import android.content.ContentValues
import android.content.Context
import androidx.annotation.RawRes
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.inglarna.blackeagle.QueryPreferences
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.model.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

@Database(entities = arrayOf(Card::class, Deck::class, WordNumber::class, Folder::class, FolderDeckCrossRef::class, Stat::class), version = 1)
abstract class BlackEagleDatabase :RoomDatabase(){
    abstract fun deckDao(): DeckDao
    abstract fun cardDao(): CardDao
    abstract fun folderDao(): FolderDao
    abstract  fun wordNumberDao(): WordNumberDao
    abstract fun statDao(): StatDao

    companion object{
        private var instance: BlackEagleDatabase? = null
        private const val TAG = "BlackEagleDatabase"
        fun getInstance(context: Context): BlackEagleDatabase{
            if(instance == null){
                var rdc = object: Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        //Insert default number words in database
                        var contentValues : ContentValues
                        var words = loadDefaultNumberWords(context)
                        for(i in words.indices){
                            contentValues = ContentValues()
                            contentValues.put("number", i)
                            contentValues.put("word", words[i])
                            db.insert("WordNumber", OnConflictStrategy.REPLACE, contentValues)
                        }
                    }
                }
                instance = Room.databaseBuilder(context.applicationContext,BlackEagleDatabase::class.java,"Database")
                    .addCallback(rdc)
                    .build()
            }
            return instance as BlackEagleDatabase
        }
        private fun loadDefaultNumberWordsFromFile(@RawRes id: Int, context: Context) : List<String>{
            val inputStream = context.resources.openRawResource(id)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var word: String?
            val words = ArrayList<String>()
            while(true){
                try {
                    word = reader.readLine()
                    if(word == null){
                        break
                    }
                    words.add(word)
                }catch (e : IOException){
                    e.printStackTrace()
                }

            }
            return words
        }
        fun loadDefaultNumberWords(context: Context) : List<String>{
            return loadDefaultNumberWordsFromFile(getWordNumberResource(context), context)
        }
        private fun getWordNumberResource(context: Context) : Int{
            return when(QueryPreferences.getNumberWordsLanguage(context)) {
                "sv" -> R.raw.number_words_swedish
                "en" -> R.raw.number_words_english
                else -> R.raw.number_words_english
            }
        }
    }

}