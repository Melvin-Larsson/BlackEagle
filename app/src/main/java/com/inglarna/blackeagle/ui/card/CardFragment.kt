package com.inglarna.blackeagle.ui.card

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.ImageStorage
import com.inglarna.blackeagle.PictureUtils
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.viewmodel.CardViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class CardFragment : Fragment() {
    lateinit var binding : FragmentCardBinding
    private val cardViewModel by viewModels<CardViewModel>()
    private var deckId: Long= -1
    private var cardId: Long = -1
    private var isEditingCard = false
    private lateinit var card : Card
    private val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

    companion object{
        private const val TAG = "CardFragment"
        private const val DECK_ID = "deckID"
        private const val CARD_ID = "cardID"

        fun newInstance(id: Long): CardFragment {
            val cardFragment = CardFragment()
            val bundle = Bundle()
            bundle.putLong(DECK_ID, id)
            cardFragment.arguments = bundle
            return cardFragment
        }
        fun newInstance(deckId: Long, cardId: Long): CardFragment{
            val cardFragment = CardFragment()
            val bundle = Bundle()
            bundle.putLong(DECK_ID, deckId)
            bundle.putLong(CARD_ID, cardId)
            cardFragment.arguments = bundle
            return cardFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCardBinding.inflate(inflater, container, false)
        val editText = object : AppCompatEditText(context!!) {

            override fun onCreateInputConnection(editorInfo: EditorInfo): InputConnection? {
                val ic: InputConnection? = super.onCreateInputConnection(editorInfo)
                EditorInfoCompat.setContentMimeTypes(editorInfo, arrayOf("image/png"))

                val callback =
                    InputConnectionCompat.OnCommitContentListener { inputContentInfo, flags, opts ->
                        val lacksPermission = (flags and
                                InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0
                        // read and display inputContentInfo asynchronously
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && lacksPermission) {
                            try {
                                inputContentInfo.requestPermission()
                            } catch (e: Exception) {
                                return@OnCommitContentListener false // return false if failed
                            }
                        }
                        // read and display inputContentInfo asynchronously.
                        // call inputContentInfo.releasePermission() as needed.
                        true  // return true if succeeded
                    }
                return ic?.let { InputConnectionCompat.createWrapper(it, editorInfo, callback) }
            }
        }
        /*
        val parentLayout = binding.Layout
        val set = ConstraintSet()
        editText.id = 1
        parentLayout.addView(editText, 0)

        set.clone(parentLayout)
        set.connect(
            editText.getId(),
            ConstraintSet.TOP,
            parentLayout.getId(),
            ConstraintSet.TOP,
            60
        )
        set.applyTo(parentLayout)

        binding.Layout.addView(editText)*/
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deckId = arguments!!.getLong(DECK_ID, -1)
        cardId = arguments!!.getLong(CARD_ID, -1)
        //If card is being added
        if (cardId == -1L){
            isEditingCard = false
            activity?.title = context?.getString(R.string.add_card)
            binding.buttonAddCard.text = context?.getString(R.string.add_card)
        }
        //If card is being edited
        else{
            isEditingCard = true
            activity?.title = context?.getString(R.string.edit_card)
            binding.buttonAddCard.text = context?.getString(R.string.edit_card)
        }
        if(cardId != -1L){
           cardViewModel.getCard(cardId).observe(this, {card ->
               this.card = card
               binding.editTextQuestion.setText(card.question)
               binding.editTextAnswer.setText(card.answer)
               binding.editTextHint.setText(card.hint)
               convertFieldsFromHtml()
            })
        }else{
            card = Card()
        }

        binding.buttonAddCard.setOnClickListener{
            saveCard()
            if(!isEditingCard){
                reset()
            }
        }
        binding.imageButtonQuestion.setOnClickListener{
            startGalleryResultQuestion.launch(gallery)
        }
        binding.imageButtonAnswer.setOnClickListener{
            startGalleryResultAnswer.launch(gallery)
        }
        binding.imageButtonHint.setOnClickListener{
            startGalleryResultHint.launch(gallery)
        }
        binding.switchShowHtml.setOnCheckedChangeListener{_, state ->
            if(state){
                convertFieldsToHtml()
            }else{
                convertFieldsFromHtml()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeUnusedImages()
    }

    private fun saveCard(){
        val regexPattern = Regex("^\\s*$")
        if (!regexPattern.matches(binding.editTextAnswer.text.toString()) &&
            !regexPattern.matches(binding.editTextQuestion.text.toString())) {

            card.deckId = deckId
            if(!binding.switchShowHtml.isChecked){
                convertFieldsToHtml()
            }
            card.question = binding.editTextQuestion.text.toString().trim()
            card.answer = binding.editTextAnswer.text.toString().trim()
            card.hint = binding.editTextHint.text.toString().trim()
            removeUnusedImages()
            if(!binding.switchShowHtml.isChecked){
                convertFieldsFromHtml()
            }
            GlobalScope.launch {
                //Add card
                if(card.id == null){
                    card.position = cardViewModel.getMaxPosition(deckId) + 1
                    cardViewModel.addCard(card)
                    linkImageToCard(card)
                }
                //Update card
                else{
                    cardViewModel.updateCard(card)
                }
            }
            Toast.makeText(requireContext(), "du lade till ett kort", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), "du din fuling, fyll i fälten", Toast.LENGTH_SHORT).show()
        }
    }
    private fun reset(){
        binding.editTextQuestion.setText("")
        binding.editTextAnswer.setText("")
        binding.editTextHint.setText("")
        card = Card()
    }

    private val startGalleryResultQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            //Images are saved at once since the user might delete them from the device before the cards is saved
            val imageName = saveImage(uri!!)
            if(!binding.switchShowHtml.isChecked){
                convertFieldsToHtml()
            }
            binding.editTextQuestion.append("<img src=\"${imageName}\">")
            if(!binding.switchShowHtml.isChecked){
                convertFieldsFromHtml()
            }
        }
    }
    private val startGalleryResultAnswer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            //Images are saved at once since the user might delete them from the device before the cards is saved
            val imageName = saveImage(uri!!)
            if(!binding.switchShowHtml.isChecked){
                convertFieldsToHtml()
            }
            binding.editTextAnswer.append("<img src=\"${imageName}\">")
            if(!binding.switchShowHtml.isChecked){
                convertFieldsFromHtml()
            }
        }
    }
    private val startGalleryResultHint = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            //Images are saved at once since the user might delete them from the device before the cards is saved
            val imageName = saveImage(uri!!)
            if(!binding.switchShowHtml.isChecked){
                convertFieldsToHtml()
            }
            binding.editTextHint.append("<img src=\"${imageName}\">")
            if(!binding.switchShowHtml.isChecked){
                convertFieldsFromHtml()
            }
        }
    }
    private fun saveImage(uri: Uri): String{
        val imageName = cardId.toString() + "_" + UUID.randomUUID().toString()
        ImageStorage.saveToInternalStorage(context!!, uri, imageName)
        return imageName
    }
    /*private fun saveImages(){
        for((imageName, uri) in images){
            if(isImageUsed(imageName)){
                ImageStorage.saveToInternalStorage(context!!, uri, imageName)
            }
        }
    }
    private fun isImageUsed(imageName: String): Boolean{
        val texts: Array<String> = arrayOf(binding.editTextQuestion.text.toString(), binding.editTextAnswer.text.toString(), binding.editTextHint.text.toString())
        val imageTag = "<img src=\"$imageName\"/>"
        for (text in texts){
            if(text.contains(imageTag)){
                return true
            }
        }
        return false
    }*/
    private fun removeUnusedImages(){
        val images = findUnusedImages()
        for(image in images){
            image.delete()
        }
    }
    private fun findUnusedImages(): List<File>{
        val images = PictureUtils.getImageFilesFromId(context!!.filesDir, cardId)
        val unusedImage = images.toMutableList()
        val texts: Array<String> = arrayOf(card.question, card.answer, card.hint)
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
        val unlinkedImages = PictureUtils.getImageFilesFromId(context!!.filesDir, -1)
        val idRegex = Regex("^-?\\d+")
        for(image in unlinkedImages){
            Log.d(TAG, "unlinked: " + image.path)
            val newName = image.name.replace(idRegex, card.id.toString())
            //Rename references in text
            val oldImgTag = "<img src=\"${image.name}\">"
            val newImgTag = "<img src=\"${newName}\">"
            card.question = card.question.replace(oldImgTag, newImgTag)
            card.answer = card.answer.replace(oldImgTag, newImgTag)
            card.hint = card.hint.replace(oldImgTag, newImgTag)
            cardViewModel.updateCard(card)
            //Rename images
            val newFile = File(image.parent, newName)
            image.renameTo(newFile)
        }
    }

    private val imageGetter = object: Html.ImageGetter {
        override fun getDrawable(source: String): Drawable{
            val drawable = BitmapDrawable(resources, BitmapFactory.decodeFile(File(context?.filesDir, source).path))
            drawable.setBounds(0,0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            return drawable
        }
    }
    private fun convertFieldsFromHtml(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            binding.editTextAnswer.setText(Html.fromHtml(binding.editTextAnswer.text.toString(), Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim())
            binding.editTextQuestion.setText(Html.fromHtml(binding.editTextQuestion.text.toString(), Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim())
            binding.editTextHint.setText(Html.fromHtml(binding.editTextHint.text.toString(), Html.FROM_HTML_MODE_COMPACT, imageGetter,null).trim())
        }else{
            binding.editTextAnswer.setText(Html.fromHtml(binding.editTextAnswer.text.toString(), imageGetter,null).trim())
            binding.editTextQuestion.setText(Html.fromHtml(binding.editTextQuestion.text.toString(), imageGetter,null).trim())
            binding.editTextHint.setText(Html.fromHtml(binding.editTextHint.text.toString(), imageGetter,null).trim())
        }
    }
    private fun convertFieldsToHtml(){
        binding.editTextAnswer.clearComposingText()
        binding.editTextQuestion.clearComposingText()
        binding.editTextHint.clearComposingText()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            binding.editTextAnswer.setText(Html.toHtml(binding.editTextAnswer.text, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE).trim())
            binding.editTextQuestion.setText(Html.toHtml(binding.editTextQuestion.text, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE).trim())
            binding.editTextHint.setText(Html.toHtml(binding.editTextHint.text, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE).trim())
        }else{
            binding.editTextAnswer.setText(Html.toHtml(binding.editTextAnswer.text).trim())
            binding.editTextQuestion.setText(Html.toHtml(binding.editTextQuestion.text).trim())
            binding.editTextHint.setText(Html.toHtml(binding.editTextHint.text).trim())
        }
        removeTrailingNewLines()
    }
    private fun removeTrailingNewLines(){
        val regex = Regex("(<br>)+$")
        binding.editTextQuestion.setText(binding.editTextQuestion.text.toString().replace(regex, ""))
        binding.editTextAnswer.setText(binding.editTextAnswer.text.toString().replace(regex, ""))
        binding.editTextHint.setText(binding.editTextHint.text.toString().replace(regex, ""))
    }
}