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
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentCardBinding
import com.inglarna.blackeagle.ui.convertnumber.NumberConverterFragment

class CardFragment : Fragment() {
    lateinit var binding : FragmentCardBinding
    private lateinit var cardViewModel: CardViewModel

    private val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

    private val startGalleryResultQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            //Images are saved at once since the user might delete them from the device before the card is saved
            val uri = result.data?.data!!
            saveImage(CardViewModel.QUESTION, uri)
        }
    }
    private val startGalleryResultAnswer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            //Images are saved at once since the user might delete them from the device before the card is saved
            val uri = result.data?.data!!
            saveImage(CardViewModel.ANSWER, uri)
        }
    }
    private val startGalleryResultHint = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            //Images are saved at once since the user might delete them from the device before the card is saved
            val uri = result.data?.data!!
            saveImage(CardViewModel.HINT, uri)
        }
    }

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
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deckId = requireArguments().getLong(DECK_ID, -1)
        val cardId = requireArguments().getLong(CARD_ID, -1)
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

        //Convert numbers to words
        binding.convertNumberButton.setOnClickListener{
            val dialog = NumberConverterFragment.newInstance()
            dialog.show(parentFragmentManager, "dialog")
        }
        setFragmentResultListener(NumberConverterFragment.REQUEST_WORDS){ _, bundle ->
            bundle.getString(NumberConverterFragment.EXTRA_WORDS)?.let { words ->
                val editTexts = arrayOf(binding.editTextQuestion, binding.editTextAnswer, binding.editTextHint)
                for (editText in editTexts){
                    //If the cursor is places in an edit text, inset the words
                    if(editText.hasFocus()){
                        editText.text?.insert(editText.selectionStart, words)
                    }
                }

            }
        }
        //Disable convert number button when no field is being edited
        //FIXME: Temporary fix, don't forget code in viewModel
        val textFieldFocusListener = View.OnFocusChangeListener { _, _ ->
            val editTexts = arrayOf(binding.editTextQuestion, binding.editTextAnswer, binding.editTextHint)
            var isFieldBeingEdited = false
            for(editText in editTexts){
                if(editText.hasFocus()){
                    isFieldBeingEdited = true
                    break
                }
            }
            cardViewModel.setIsFieldBeingEdited(isFieldBeingEdited)
        }
        binding.editTextQuestion.onFocusChangeListener = textFieldFocusListener
        binding.editTextAnswer.onFocusChangeListener = textFieldFocusListener
        binding.editTextHint.onFocusChangeListener = textFieldFocusListener
    }

    override fun onDestroy() {
        super.onDestroy()
        cardViewModel.removeUnusedImages() //FIXME: This safe?
    }


    /**
     * Adds an image tag to the specified field and saves the image in local storage
     * @param field the field to append the image tag to. Either CardViewModel.QUESTION, CardViewModel.ANSWER or CardViewModel.HINT
     * @param uri the Uri of the image that is to be saved
     */
    private fun saveImage(field: Int, uri: Uri){
        val size = Point()
        requireActivity().windowManager.defaultDisplay.getSize(size)

        val imageName = cardViewModel.saveImage(uri, size.x/2, size.y)
        cardViewModel.appendHtml(field, "<img src=\"${imageName}\">")
    }

}