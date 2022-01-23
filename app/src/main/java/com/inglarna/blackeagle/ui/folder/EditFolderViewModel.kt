package com.inglarna.blackeagle.ui.folder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import com.inglarna.blackeagle.repository.FolderRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditFolderViewModel(application: Application, val folderId: Long): AndroidViewModel(application) {
    private val folderRepo = FolderRepo(application)

    val folder = folderRepo.getFolder(folderId)

    fun deleteFolder(){
        GlobalScope.launch {
            if(folder.value != null){
                folderRepo.deleteFolder(folder.value!!)
            }
        }
    }
    fun renameFolder(name: String){
        GlobalScope.launch {
            if(folder.value != null){
                folder.value!!.name = name
                folderRepo.updateFolder(folder.value!!)
            }
        }
    }
}