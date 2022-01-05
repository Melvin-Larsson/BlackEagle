package com.inglarna.blackeagle.ui.addcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.databinding.FragmentEditCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.viewmodel.CardViewModel
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddCardFragment : Fragment() {
    lateinit var binding : FragmentEditCardBinding
    private val cardViewModel by viewModels<CardViewModel>()
    private val deckViewModel by viewModels<DeckViewModel>()
    private var deckId: Long= -1


    companion object{
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
                    card.position = deckViewModel.getDeckSize(deckId)
                    cardViewModel.addCard(card)
                }
                binding.editTextAnswer.setText("")
                binding.editTextQuestion.setText("")
                binding.hint.setText("")
                Toast.makeText(requireContext(), "du lade till ett kort", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "du din fuling, fyll i fälten", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}