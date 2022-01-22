package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.*

class FolderRepo(context: Context) {
    private val db = BlackEagleDatabase.getInstance(context)
    private val folderDao = db.folderDao()

    fun getFolders(): LiveData<List<Folder>>{
        return folderDao.getFolders()
    }
    fun getFolder(id: Long): LiveData<Folder>{
        return folderDao.getFolder(id)
    }
    fun getFolder(search: String): LiveData<Folder>{
        return folderDao.getFolder(search)
    }
    fun getFolderWithDecks(folderId: Long): LiveData<FolderWithDecks>{
        return folderDao.getFolderWithDecks(folderId)
    }
    fun getAllFoldersWithDecks(): LiveData<List<FolderWithDecks>>{
        return folderDao.getAllFoldersWithDecks()
    }
    fun getDeckWithFolders(folderId: Long): LiveData<DeckWithFolders>{
        return folderDao.getDeckWithFolders(folderId)
    }
    fun getAllDecksWithFolders(): LiveData<List<DeckWithFolders>>{
        return folderDao.getAllDecksWithFolders()
    }
    fun getDecksOutsideOfFolder(folderId: Long): LiveData<List<Deck>>{
        return folderDao.getDecksOutsideOfFolder(folderId)
    }
    fun addFolder(folder: Folder): Long{
        return folderDao.addFolder(folder)
    }
    fun updateFolder(folder: Folder){
        folderDao.updateFolder(folder)
    }
    fun addDeckToFolder(deck: Deck, folder: Folder){
        addDeckToFolder(deck.deckId!!, folder.folderId!!)
    }
    fun addDeckToFolder(deckId: Long, folderId: Long){
        folderDao.addFolderDeckCrossRef(FolderDeckCrossRef(folderId, deckId))
    }
    fun removeDeckFromFolder(folderId: Long, deckId: Long){
        folderDao.removeFolderDeckCrossRef(folderId, deckId)
    }

}