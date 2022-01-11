package com.inglarna.blackeagle.model

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File
import java.util.*
import kotlin.math.ln

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var deckId: Long? = null,
    var question : String = "",
    var answer : String = "",
    var hint : String = "",
    var position : Int = 0,
    var firstRepetition : Double? = null,
    var nextRepetition : Double = millisToDays(Date().time).toDouble(),
    var k : Double = 1.0,
){
    companion object{
        private const val TAG = "Card"
        private const val NEXT_REPETITION_RETRIEVABILITY  = 0.7
        private val rand = Random()
        private fun millisToDays(millis : Long) : Long{
            return millis / (1000 * 3600 * 24)
        }
    }
    fun getImageFreeQuestionFromHtml(context: Context) = fromHtml(context, replaceImages(question))
    fun getImageFreeAnswerFromHtml(context: Context) = fromHtml(context, replaceImages(answer))
    fun getImageFreeHintFromHtml(context: Context) = fromHtml(context, replaceImages(hint))
    fun getQuestionFromHtml(context: Context) = fromHtml(context, question)
    fun getAnswerFromHtml(context: Context) = fromHtml(context, answer)
    fun getHintFromHtml(context: Context) = fromHtml(context, hint)
    private fun fromHtml(context: Context, html: String): CharSequence {
        val imageGetter = object: Html.ImageGetter {
            override fun getDrawable(source: String): Drawable {
                val drawable = BitmapDrawable(context.resources, BitmapFactory.decodeFile(File(context.filesDir, source).path))
                drawable.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                return drawable
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim()
        }
        return Html.fromHtml(html,imageGetter,null).trim()
    }
    private fun replaceImages(string: String): String{
        val imageRegex = Regex("<img src=\".*\">")
        return string.replace(imageRegex, "(img)")
    }

    fun repeated(retrieveability: Double){
        if(retrieveability <= 0){
            firstRepetition = null
            nextRepetition = millisToDays(Date().time) + rand.nextDouble()
            k = 1.0
        }else{
            if(firstRepetition == null){
                firstRepetition = millisToDays(Date().time).toDouble()
                nextRepetition = firstRepetition!! + 1
            }else{
                k *= NEXT_REPETITION_RETRIEVABILITY
                var t = ln(k/0.8584) / -0.0881
                nextRepetition = t.toLong() + firstRepetition!!
            }
        }
    }
}
