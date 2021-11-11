package com.inglarna.blackeagle.ui.cardlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.databinding.FragmentCardListBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.addcard.AddCardActivity

class CardListFragment : Fragment() {
    lateinit var binding : FragmentCardListBinding
    lateinit var onAddCardClicked: (()-> Unit)

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerViewCard.adapter = CardListRecyclerViewAdapter()
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonAddCard.setOnClickListener{
            onAddCardClicked()
        }
    }
}