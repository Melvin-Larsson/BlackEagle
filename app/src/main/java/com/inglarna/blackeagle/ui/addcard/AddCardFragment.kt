package com.inglarna.blackeagle.ui.addcard

import android.app.Activity.RESULT_OK
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.ImageStorage
import com.inglarna.blackeagle.databinding.FragmentEditCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.viewmodel.CardViewModel
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//TODO: spara bilderna melvin
class AddCardFragment : Fragment() {
    lateinit var binding : FragmentEditCardBinding
    private val cardViewModel by viewModels<CardViewModel>()
    private val deckViewModel by viewModels<DeckViewModel>()
    private var deckId: Long= -1
    private var imageUriQuestion: Uri? = null
    private var imageUriAnswer: Uri? = null
    private var imageUriHint: Uri? = null
    private var imageFileNameQuestion = ""
    private var imageFileNameAnswer = ""
    private var imageFileNameHint = ""
    val gallary = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
    val startGalleryResultQuestion = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            imageUriQuestion = result.data?.data
            binding.imageQuestion.setImageURI(imageUriQuestion)
        }
    }
    val startGalleryResultAnswer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            imageUriAnswer = result.data?.data
            binding.imageAnswer.setImageURI(imageUriAnswer)
        }
    }
    val startGalleryResultHint = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            imageUriHint = result.data?.data
            binding.imageHint.setImageURI(imageUriHint)
        }
    }

    companion object{
        private const val TAG = "addCardFragment"
        private const val DECK_ID = "deckID"
        fun newInstance(id: Long): AddCardFragment {
            val addCardFragment = AddCardFragment()
            val bundle = Bundle()
            bundle.putLong(DECK_ID, id)
            addCardFragment.arguments = bundle
            return addCardFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        deckId = arguments!!.getLong(DECK_ID, -1)
        binding = FragmentEditCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddCard.setOnClickListener{
            val regexPattern = Regex("^\\s*$")
            if (!regexPattern.matches(binding.editTextAnswer.text.toString()) &&
                !regexPattern.matches(binding.editTextQuestion.text.toString())) {
                val question = binding.editTextQuestion.text.toString()
                val answer = binding.editTextAnswer.text.toString()
                val hint = binding.hint.text.toString()

                val card = Card()
                card.deckId = deckId
                card.question = question
                card.answer = answer
                card.hint = hint
                GlobalScope.launch {
                    card.position = cardViewModel.getMaxPosition(deckId) + 1
                    cardViewModel.addCard(card)
                    Log.d(TAG, card.id.toString())
                    imageFileNameQuestion = card.id.toString() + "question"
                    imageFileNameAnswer = card.id.toString() + "Answer"
                    imageFileNameHint = card.id.toString() + "Hint"

                    if(imageUriQuestion != null){ ImageStorage.saveToInternalStorage(context!!, imageUriQuestion!!, imageFileNameQuestion)}
                    if (imageUriAnswer != null){ImageStorage.saveToInternalStorage(context!!, imageUriAnswer!!, imageFileNameAnswer)}
                    if(imageUriHint != null){ ImageStorage.saveToInternalStorage(context!!, imageUriHint!!, imageFileNameHint)}
                }
                binding.editTextQuestion.setText("")
                binding.editTextAnswer.setText("")
                binding.hint.setText("")

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
            startGalleryResultQuestion.launch(gallary)
        }
        binding.imageButtonAnswer.setOnClickListener{
            startGalleryResultAnswer.launch(gallary)
        }
        binding.imageButtonHint.setOnClickListener{
            startGalleryResultHint.launch(gallary)
        }
    }
}