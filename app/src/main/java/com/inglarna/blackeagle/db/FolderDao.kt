package com.inglarna.blackeagle.db

import android.icu.text.CaseMap
import androidx.lifecycle.LiveData
import androidx.room.*
import com.inglarna.blackeagle.model.DeckWithFolders
import com.inglarna.blackeagle.model.Folder
import com.inglarna.blackeagle.model.FolderDeckCrossRef
import com.inglarna.blackeagle.model.FolderWithDecks

@Dao
interface FolderDao {
    @Query("SELECT * FROM Folder")
    fun getFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM Folder WHERE folderId=:id")
    fun getFolder(id: Long): LiveData<Folder>

    @Query("SELECT * FROM Folder WHERE name LIKE '%' | :search |'%' ")
    fun getFolder(search: String): LiveData<Folder>

    @Transaction
    @Query("SELECT * FROM Folder WHERE folderId=:folderId")
    fun getFolderWithDecks(folderId: Long): LiveData<FolderWithDecks>

    @Transaction
    @Query("SELECT * FROM Folder")
    fun getAllFoldersWithDecks(): LiveData<List<FolderWithDecks>>

    @Transaction
    @Query("SELECT * FROM Deck WHERE deckId=:deckId")
    fun getDeckWithFolders(deckId: Long): LiveData<DeckWithFolders>

    @Transaction
    @Query("SELECT * FROM Deck")
    fun getAllDecksWithFolders(): LiveData<List<DeckWithFolders>>

    @Insert
    fun addFolder(folder: Folder): Long

    @Insert
    fun addFolderDeckCrossRef(folderDeckCrossRef: FolderDeckCrossRef)

    @Query("DELETE FROM FolderDeckCrossRef WHERE folderId=:folderId AND deckId=:deckId")
    fun removeFolderDeckCrossRef(folderId: Long, deckId: Long)

    @Update
    fun updateFolder(folder: Folder)
}