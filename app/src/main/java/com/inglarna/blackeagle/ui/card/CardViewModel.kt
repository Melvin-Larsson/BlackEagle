package com.inglarna.blackeagle.ui.card

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.text.*
import android.util.Log
import androidx.core.text.toSpanned
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.inglarna.blackeagle.HtmlUtils
import com.inglarna.blackeagle.ImageStorage
import com.inglarna.blackeagle.PictureUtils
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.repository.CardRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CardViewModel (val app: Application, val deckId: Long, val cardId: Long) : AndroidViewModel(app){//FIXME: why not application?
    private var cardRepo: CardRepo = CardRepo(getApplication())

    val card = cardRepo.getCard(cardId)

    val isEditingCard = card.map {card ->
        if(card != null){ //FIXME: does not really make sense to put this here
            question.value = card.question
            answer.value = card.answer
            hint.value = card.hint
            showHtml(false)

        }
        card != null
    }
    //TODO: Two way data binding: https://developer.android.com/topic/libraries/data-binding/two-way
    val question = MutableLiveData<CharSequence>("")
    val answer = MutableLiveData<CharSequence>("")
    val hint = MutableLiveData<CharSequence>("")

    private val _questionError = MutableLiveData<Int?>(null)
    val questionError: LiveData<Int?>
        get() = _questionError

    private val _answerError = MutableLiveData<Int?>(null)
    val answerError: LiveData<Int?>
        get() = _answerError

    private var isHtmlShowing = false

    companion object{
        const val QUESTION = 0
        const val ANSWER = 1
        const val HINT = 2
    }

    /**
     * Saves the contents of the fields: question, hint and answer to a new card or updates an old card if such exists
     */
    fun save(){
        var errors = false
        //Check if question field is filled
        if(question.value!!.isEmpty()){
            _questionError.value = R.string.error_field_empty
            errors = true
        }else{
            _questionError.value = null
        }
        //Check if answer field is filled
        if(answer.value!!.isEmpty()){
            _answerError.value = R.string.error_field_empty
            errors = true
        }else{
            _answerError.value = null
        }
        if(errors){
            return
        }

        removeUnusedImages()

        //Get hint value
        val hintString =  if(hint.value != null && hint.value!!.isNotEmpty()){hint.value!!}else{""}

        //Use the old card if it exits, otherwise create a new one
        val saveCard = if(card.value != null){
            card.value!!
        }else{
            Card()
        }

        //Set properties
        saveCard.question = getHtml(QUESTION)
        saveCard.answer = getHtml(ANSWER)
        saveCard.hint = getHtml(HINT)
        saveCard.deckId = deckId

        //Save card to database
        GlobalScope.launch {
            //Add card
            if(card.value == null){
                cardRepo.addCard(saveCard)
            }
            //Update card
            else{
                cardRepo.updateCard(saveCard)
            }
            linkImageToCard(saveCard)
        }

        //Clear fields
        question.value = ""
        answer.value = ""
        hint.value = ""

    }
    private fun updateCard(card: Card){
        cardRepo.updateCard(card)
    }

    fun showHtml(showHtml: Boolean){
        isHtmlShowing = showHtml
        if(showHtml){
            question.value = HtmlUtils.toHtml(question.value!!.toSpanned())
            answer.value = HtmlUtils.toHtml(answer.value!!.toSpanned())
            hint.value = HtmlUtils.toHtml(hint.value!!.toSpanned())
        }else{
            question.value = HtmlUtils.fromHtml(getApplication(), question.value.toString())
            answer.value = HtmlUtils.fromHtml(getApplication(), answer.value.toString())
            hint.value = HtmlUtils.fromHtml(getApplication(), hint.value.toString())
        }
    }

    /**Appends html to the specified field. If the field is in display format (not html) it is converted to html before the string is appended.
     * Then it is converted back to its original format.
     * @param field the field to append to, is either QUESTION, ANSWER or HINT
     * @param html the html to append
     */
    fun appendHtml(field: Int, html: String){
        val subject = getSubject(field) ?: return
        if(isHtmlShowing){
            val newHtml = subject.value.toString() + html
            subject.value = newHtml
        }else{
            val newHtml = HtmlUtils.toHtml(subject.value!!.toSpanned()).toString() + html
            subject.value = HtmlUtils.fromHtml(getApplication(), newHtml)
        }
    }
    private fun getHtml(field: Int): String{
        val subject = getSubject(field) ?: return ""
        return if(isHtmlShowing){
            subject.value.toString()
        }else{
            HtmlUtils.toHtml(subject.value!!.toSpanned())
        }
    }
    private fun getSubject(field: Int): MutableLiveData<CharSequence>?{
        return when(field){
            QUESTION -> question
            ANSWER -> answer
            HINT -> hint
            else -> null
        }
    }

    fun saveImage(uri: Uri, destWidth: Int, desHeight: Int): String{
        val bitmap = PictureUtils.getScaledBitmap(getApplication(), uri, destWidth, desHeight)

        val imageName = cardId.toString() + "_" + UUID.randomUUID().toString()
        val pathName = File(app.filesDir, imageName).toString()

        val out = FileOutputStream(pathName)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.close()

        return imageName
    }

    fun removeUnusedImages(){
        val images = findUnusedImages()
        for(image in images){
            image.delete()
        }
    }

    /**
     * Returns a list with image files in internal storage that has the id of the current card but are not referenced
     * in any image tag in the question, answer or hint.
     */
    private fun findUnusedImages(): List<File>{
        val images = PictureUtils.getImageFilesFromId(app.filesDir, cardId)
        val unusedImage = images.toMutableList()
        val texts: Array<String> = arrayOf(
            getHtml(QUESTION),
            getHtml(ANSWER),
            getHtml(HINT)
        )
        for(image in images){
            val imageTag = "<img src=\"${image.name}\">"
            for(text in texts){
                if(text.contains(imageTag)){
                    unusedImage.remove(image)
                }
            }
        }
        return unusedImage
    }

    private fun linkImageToCard(card: Card){
        val unlinkedImages = PictureUtils.getImageFilesFromId(app.filesDir, -1)
        val idRegex = Regex("^-?\\d+")
        for(image in unlinkedImages){
            val newName = image.name.replace(idRegex, card.cardId.toString())
            //Rename references in text
            val oldImgTag = "<img src=\"${image.name}\">"
            val newImgTag = "<img src=\"${newName}\">"
            card.question = card.question.replace(oldImgTag, newImgTag)
            card.answer = card.answer.replace(oldImgTag, newImgTag)
            card.hint = card.hint.replace(oldImgTag, newImgTag)
            updateCard(card)
            //Rename images
            val newFile = File(image.parent, newName)
            image.renameTo(newFile)
        }
    }

}