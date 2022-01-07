package com.inglarna.blackeagle.ui.card

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.inglarna.blackeagle.databinding.FragmentCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.viewmodel.CardViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.net.toUri
import com.inglarna.blackeagle.PictureUtils


//TODO: spara bilderna melvin
class CardFragment : Fragment() {
    lateinit var binding : FragmentCardBinding
    private val cardViewModel by viewModels<CardViewModel>()
    private var deckId: Long= -1
    private var cardId: Long = -1
    private var card : Card? = null
    private var imageUriQuestion: Uri? = null
    private var imageUriAnswer: Uri? = null
    private var imageUriHint: Uri? = null
    private var imageFileNameQuestion = ""
    private var imageFileNameAnswer = ""
    private var imageFileNameHint = ""
    private val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
    private val startGalleryResultQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            imageUriQuestion = result.data?.data
            binding.imageQuestion.setImageURI(imageUriQuestion)
        }
    }
    private val startGalleryResultAnswer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            imageUriAnswer = result.data?.data
            binding.imageAnswer.setImageURI(imageUriAnswer)
        }
    }
    private val startGalleryResultHint = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            imageUriHint = result.data?.data
            binding.imageHint.setImageURI(imageUriHint)
        }
    }

    companion object{
        private const val TAG = "addCardFragment"
        private const val DECK_ID = "deckID"
        private const val CARD_ID = "cardID"
        private const val ANSWER_FILE_IDENTIFIER = "answer"
        private const val QUESTION_FILE_IDENTIFIER = "question"
        private const val HINT_FILE_IDENTIFIER = "hint"
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
        if(cardId != -1L){
           cardViewModel.getCard(cardId).observe(this, {card ->
               this.card = card
               binding.editTextQuestion.setText(card.question)
               binding.editTextAnswer.setText(card.answer)
               binding.editTextHint.setText(card.hint)
            })
            loadImages()
        }

        binding.buttonAddCard.setOnClickListener{
            val regexPattern = Regex("^\\s*$")
            if (!regexPattern.matches(binding.editTextAnswer.text.toString()) &&
                !regexPattern.matches(binding.editTextQuestion.text.toString())) {
                val question = binding.editTextQuestion.text.toString()
                val answer = binding.editTextAnswer.text.toString()
                val hint = binding.editTextHint.text.toString()

                val newCard = if(card == null){
                    Card()
                }else{
                    card
                }
                newCard?.deckId = deckId
                newCard?.question = question
                newCard?.answer = answer
                newCard?.hint = hint
                GlobalScope.launch {
                    if(card == null){
                        newCard?.position = cardViewModel.getMaxPosition(deckId) + 1
                        cardViewModel.addCard(newCard!!)
                    }else{
                        cardViewModel.updateCard(newCard!!)
                    }

                    imageFileNameQuestion = newCard?.id.toString() + QUESTION_FILE_IDENTIFIER
                    imageFileNameAnswer = newCard?.id.toString() + ANSWER_FILE_IDENTIFIER
                    imageFileNameHint = newCard?.id.toString() + HINT_FILE_IDENTIFIER

                    if(imageUriQuestion != null){ ImageStorage.saveToInternalStorage(context!!, imageUriQuestion!!, imageFileNameQuestion)}
                    if (imageUriAnswer != null){ImageStorage.saveToInternalStorage(context!!, imageUriAnswer!!, imageFileNameAnswer)}
                    if(imageUriHint != null){ ImageStorage.saveToInternalStorage(context!!, imageUriHint!!, imageFileNameHint)}
                }
                binding.editTextQuestion.setText("")
                binding.editTextAnswer.setText("")
                binding.editTextHint.setText("")

                binding.imageQuestion.setImageResource(0)
                binding.imageAnswer.setImageResource(0)
                binding.imageHint.setImageResource(0)
                binding.imageQuestion
                binding.imageAnswer.invalidate()
                Toast.makeText(requireContext(), "du lade till ett kort", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "du din fuling, fyll i fälten", Toast.LENGTH_SHORT).show()
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
    }
    private fun loadImages(){
        val imageFiles = PictureUtils.getImageFilesFromId(context!!.filesDir, cardId)
        for(file in imageFiles){
            Log.d(TAG, file.name)
            if(file.name.contains(QUESTION_FILE_IDENTIFIER)){
                binding.imageQuestion.setImageURI(file.toUri())
            }else if(file.name.contains(ANSWER_FILE_IDENTIFIER)){
                binding.imageAnswer.setImageURI(file.toUri())
            }else if(file.name.contains(HINT_FILE_IDENTIFIER)){
                binding.imageHint.setImageURI(file.toUri())
            }
        }

    }
}