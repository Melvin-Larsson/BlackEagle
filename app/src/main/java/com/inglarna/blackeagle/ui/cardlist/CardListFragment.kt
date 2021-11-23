package com.inglarna.blackeagle.ui.cardlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.databinding.FragmentCardListBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.addcard.AddCardActivity
import com.inglarna.blackeagle.viewmodel.CardViewModel
import com.inglarna.blackeagle.viewmodel.DeckViewModel

class CardListFragment : Fragment() {
    lateinit var binding : FragmentCardListBinding
    lateinit var onAddCardClicked: (()-> Unit)
    private val cardViewModel by viewModels<CardViewModel>()
    private var deckId: Long= -1

    companion object{
        private const val DECK_ID = "deckId"
        fun newInstance(deckId: Long) : CardListFragment{
            val fragment = CardListFragment();
            val bundle = Bundle();
            bundle.putLong(DECK_ID, deckId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deckId = arguments!!.getLong(DECK_ID, -1)
        binding.recyclerViewCard.adapter = CardListRecyclerViewAdapter(cardViewModel.getDeckViews(deckId), this)
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonAddCard.setOnClickListener{
            onAddCardClicked()
        }
    }
}