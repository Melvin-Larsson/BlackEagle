package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.inglarna.blackeagle.databinding.FragmentDeckListBinding
import com.inglarna.blackeagle.model.Deck

class DeckListFragment : Fragment() {
    private lateinit var binding : FragmentDeckListBinding
    lateinit var onDeckSelected: ((Deck) -> Unit)

    companion object{
        fun newInstance() = DeckListFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DeckListRecyclerViewAdapter(requireContext())
        adapter.onDeckClicked = onDeckSelected
        binding.deckRecyclerView.adapter = adapter
        binding.deckRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}