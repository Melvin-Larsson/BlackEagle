package com.inglarna.blackeagle

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.text.Html
import java.io.File

class HtmlUtils {
    companion object{
        @JvmStatic //Annotation required for the method to be accessible from data binding
        fun fromHtmlWithoutImages(context: Context, html: String?) = fromHtml(context, replaceImages(html))

        @JvmStatic
        fun fromHtml(context: Context, html: String?): CharSequence {
            if(html == null){
                return ""
            }
            val imageGetter = Html.ImageGetter { source ->
                val drawable = BitmapDrawable(context.resources, BitmapFactory.decodeFile(File(context.filesDir, source).path))
                drawable.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                drawable
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim()
            }
            return Html.fromHtml(html,imageGetter,null).trim()
        }
        @JvmStatic
        fun replaceImages(string: String?): String{
            if(string == null){
                return ""
            }
            val imageRegex = Regex("<img src=\".*\">")
            return string.replace(imageRegex, "(img)")
        }
    }
}