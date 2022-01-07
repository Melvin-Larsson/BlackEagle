package com.inglarna.blackeagle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

class PictureUtils {
    companion object{
        fun getBitmap(path: String): Bitmap{
            return  BitmapFactory.decodeFile(path)
        }
        fun getImageFilesFromId(source: File, id: Long): List<File>{
            val files = source.listFiles()
            val imageFiles = ArrayList<File>()
            val cardIdRegex = Regex("^$id")
            if(files != null){
                for(file in files){
                    if(cardIdRegex.containsMatchIn(file.name)){
                        imageFiles.add(file)
                    }
                }
            }
            return imageFiles
        }
    }
}