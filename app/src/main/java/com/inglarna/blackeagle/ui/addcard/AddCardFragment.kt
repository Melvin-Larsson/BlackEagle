package com.inglarna.blackeagle.ui.addcard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentEditCardBinding
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.repository.DeckRepo

class AddCardFragment : Fragment() {
    lateinit var binding : FragmentEditCardBinding
    lateinit var deckRepo: DeckRepo

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
        binding = FragmentEditCardBinding.inflate(inflater, container, false)
        deckRepo = DeckRepo(requireContext())
        binding.buttonAddCard.setOnClickListener{
            var question = binding.editTextQuestion.text.toString()
            var answer = binding.editTextAnswer.text.toString()
            var hint = binding.hint.text.toString()

            var card = Card()
            card.question = question
            card.answer = answer
            card.hint = hint
        }
        return binding.root
    }
}