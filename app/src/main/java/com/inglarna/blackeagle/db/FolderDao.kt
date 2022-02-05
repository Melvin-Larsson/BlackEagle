package com.inglarna.blackeagle.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.inglarna.blackeagle.model.*

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

    @Query("SELECT * FROM Deck WHERE deckId NOT IN (SELECT deckId FROM FolderDeckCrossRef WHERE folderId=:folderId)")
    fun getDecksOutsideOfFolder(folderId: Long): LiveData<List<Deck>>

    @Insert
    fun addFolder(folder: Folder): Long

    @Insert(onConflict = REPLACE)
    fun addFolderDeckCrossRef(folderDeckCrossRef: FolderDeckCrossRef)

    @Query("DELETE FROM FolderDeckCrossRef WHERE folderId=:folderId AND deckId=:deckId")
    fun removeFolderDeckCrossRef(folderId: Long, deckId: Long)

    @Update
    fun updateFolder(folder: Folder)

    @Delete
    fun deleteFolder(folder: Folder)
}