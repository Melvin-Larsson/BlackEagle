package com.inglarna.blackeagle.ui.addcard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.databinding.FragmentEditCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.repository.DeckRepo
import com.inglarna.blackeagle.viewmodel.CardViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddCardFragment : Fragment() {
    lateinit var binding : FragmentEditCardBinding
    private val cardViewModel by viewModels<CardViewModel>()
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
            var question = binding.editTextQuestion.text.toString()
            var answer = binding.editTextAnswer.text.toString()
            var hint = binding.hint.text.toString()

            var card = Card()
            card.deckId = deckId
            card.question = question
            card.answer = answer
            card.hint = hint
            GlobalScope.launch {
                cardViewModel.addCard(card)
            }
        }
        return binding.root
    }
}