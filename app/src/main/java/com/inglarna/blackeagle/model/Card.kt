package com.inglarna.blackeagle.model

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.text.Html
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inglarna.blackeagle.intervalAlgorithm.SM2Algorithm
import getCurrentDay
import java.io.File
import java.util.*

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true) var cardId:Long? = null,
    var deckId: Long? = null,
    var question : String = "",
    var answer : String = "",
    var hint : String = "",
    var position : Int = 0,
    var lastRepetition : Double? = null,
    var nextRepetition : Double = getCurrentDay().toDouble(),
    var repetitions : Int = 0,
    var easinessFactor : Double = 2.5,
){

    companion object{
        private const val TAG = "Card"
    }

    fun repeated(difficulty: Double){
        SM2Algorithm().cardRepeated(this, difficulty)
    }
    fun isNextRepetitionToday(): Boolean{
        return nextRepetition.toLong() == getCurrentDay()
    }

    fun getImageFreeQuestionFromHtml(context: Context) = fromHtml(context, replaceImages(question))
    fun getImageFreeAnswerFromHtml(context: Context) = fromHtml(context, replaceImages(answer))
    fun getImageFreeHintFromHtml(context: Context) = fromHtml(context, replaceImages(hint))
    fun getQuestionFromHtml(context: Context) = fromHtml(context, question)
    fun getAnswerFromHtml(context: Context) = fromHtml(context, answer)
    fun getHintFromHtml(context: Context) = fromHtml(context, hint)
    private fun fromHtml(context: Context, html: String): CharSequence {
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
    private fun replaceImages(string: String): String{
        val imageRegex = Regex("<img src=\".*\">")
        return string.replace(imageRegex, "(img)")
    }

}
