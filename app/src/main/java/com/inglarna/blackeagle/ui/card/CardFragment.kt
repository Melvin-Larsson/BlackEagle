package com.inglarna.blackeagle.ui.card

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inglarna.blackeagle.ImageStorage
import com.inglarna.blackeagle.PictureUtils
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentCardBinding
import com.inglarna.blackeagle.model.Card
import java.io.File
import java.util.*

class CardFragment : Fragment() {
    lateinit var binding : FragmentCardBinding
    private lateinit var cardViewModel: CardViewModel
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
        /*val editText = object : AppCompatEditText(context!!) {

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
        }*/
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
        val deckId = arguments!!.getLong(DECK_ID, -1)
        val cardId = arguments!!.getLong(CARD_ID, -1)
        cardViewModel = ViewModelProvider(this, CardViewModelFactory(requireActivity().application, deckId, cardId))[CardViewModel::class.java]
        binding.viewModel = cardViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //Set title depending on if a card is being edited or added
        cardViewModel.isEditingCard.observe(viewLifecycleOwner){ isEditingCard ->
            if(isEditingCard){
                requireActivity().title = getString(R.string.edit_card)
            }else{
                requireActivity().title = getString(R.string.add_card)
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
        binding.switchShowHtml.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                //Remove the underscore below the word being edited. If not removed before converting to html, underline tags will be added in the html
                binding.editTextAnswer.clearComposingText()
                binding.editTextQuestion.clearComposingText()
                binding.editTextHint.clearComposingText()
            }
            cardViewModel.showHtml(isChecked)
        }
        //FIXME: tf is this horrible code
        cardViewModel.questionError.observe(viewLifecycleOwner){ error ->
            if(error == null){
                binding.textInputLayoutQuestion.error = null
            }else{
                binding.textInputLayoutQuestion.error = getString(error)
            }
        }
        cardViewModel.answerError.observe(viewLifecycleOwner){ error ->
            if(error == null){
                binding.textInputLayoutAnswer.error = null
            }else{
                binding.textInputLayoutAnswer.error = getString(error)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cardViewModel.removeUnusedImages() //FIXME: This safe?
    }

    private val startGalleryResultQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            //Images are saved at once since the user might delete them from the device before the card is saved
            val uri = result.data?.data!!
            addImage(CardViewModel.QUESTION, uri)
        }
    }
    private val startGalleryResultAnswer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            //Images are saved at once since the user might delete them from the device before the card is saved
            val uri = result.data?.data!!
            addImage(CardViewModel.ANSWER, uri)
        }
    }
    private val startGalleryResultHint = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            //Images are saved at once since the user might delete them from the device before the card is saved
            val uri = result.data?.data!!
            addImage(CardViewModel.HINT, uri)
        }
    }

    /**
     * Adds an image tag to the specified field and saves the image in local storage
     * @param field the field to append the image tag to. Either CardViewModel.QUESTION, CardViewModel.ANSWER or CardViewModel.HINT
     * @param uri the Uri of the image that is to be saved
     */
    private fun addImage(field: Int, uri: Uri){
        val size = Point()
        requireActivity().windowManager.defaultDisplay.getSize(size)

        val imageName = cardViewModel.saveImage(uri, size.x/2, size.y)
        cardViewModel.appendHtml(field, "<img src=\"${imageName}\">")
    }

}