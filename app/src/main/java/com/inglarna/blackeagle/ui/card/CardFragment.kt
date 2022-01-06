package com.inglarna.blackeagle.ui.card

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.ImageStorage
import com.inglarna.blackeagle.databinding.FragmentCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.viewmodel.CardViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCardBinding.inflate(inflater, container, false)
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
               binding.hint.setText(card.hint)
            })

        }

        binding.buttonAddCard.setOnClickListener{
            val regexPattern = Regex("^\\s*$")
            if (!regexPattern.matches(binding.editTextAnswer.text.toString()) &&
                !regexPattern.matches(binding.editTextQuestion.text.toString())) {
                val question = binding.editTextQuestion.text.toString()
                val answer = binding.editTextAnswer.text.toString()
                val hint = binding.hint.text.toString()

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
                    imageFileNameQuestion = newCard!!.id.toString() + "question"
                    imageFileNameAnswer = newCard!!.id.toString() + "Answer"
                    imageFileNameHint = newCard!!.id.toString() + "Hint"
                    ImageStorage.saveToInternalStorage(context!!, imageUriQuestion!!, imageFileNameQuestion)
                    ImageStorage.saveToInternalStorage(context!!, imageUriAnswer!!, imageFileNameAnswer)
                    ImageStorage.saveToInternalStorage(context!!, imageUriHint!!, imageFileNameHint)
                }
                binding.editTextAnswer.setText("")
                binding.editTextQuestion.setText("")
                binding.hint.setText("")
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
}