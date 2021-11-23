package com.inglarna.blackeagle.ui.addcard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentEditCardBinding
import com.inglarna.blackeagle.model.Card

class AddCardFragment : Fragment() {
    lateinit var binding : FragmentEditCardBinding

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


        binding.buttonAddCard.setOnClickListener{
            var question = binding.editTextQuestion.text
            var answer = binding.editTextAnswer.text
            var hint = binding.hint.text

            var temp = Card()
        }
        return binding.root
    }
}