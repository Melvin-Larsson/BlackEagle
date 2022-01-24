package com.inglarna.blackeagle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File
import kotlin.math.round

class PictureUtils {
    companion object{
        fun getScaledBitmap(context: Context, path: Uri, destWidth: Int, destHeight: Int): Bitmap{
            var input = context.contentResolver.openInputStream(path)
            var options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(input, null, options)
            input!!.close()

            val srcWidth = options.outWidth
            val srcHeight = options.outHeight

            var inSampleSize = 1
            if(srcHeight > destHeight || srcWidth > destWidth){
                val heightScale = srcHeight.toDouble() / destHeight
                val widthScale = srcWidth.toDouble() / destWidth

                inSampleSize = if(heightScale > widthScale){
                    round(heightScale).toInt()
                }else{
                    round(widthScale).toInt()
                }
            }
            options = BitmapFactory.Options()
            options.inSampleSize = inSampleSize;
            input = context.contentResolver.openInputStream(path)
            val bitmap = BitmapFactory.decodeStream(input, null, options)
            input!!.close()
            return bitmap!!
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