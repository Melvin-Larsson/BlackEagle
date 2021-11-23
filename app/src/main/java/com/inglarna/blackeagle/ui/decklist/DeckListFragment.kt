package com.inglarna.blackeagle.ui.decklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inglarna.blackeagle.databinding.FragmentDeckListBinding
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.viewmodel.DeckViewModel

class DeckListFragment : Fragment() {
    private lateinit var binding : FragmentDeckListBinding
    lateinit var onDeckSelected: ((DeckViewModel.DeckView) -> Unit)
    private val deckViewModel by viewModels<DeckViewModel>()
    private lateinit var deckRecyclerViewAdapter: DeckListRecyclerViewAdapter

    companion object{
        fun newInstance() = DeckListFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deckRecyclerViewAdapter = DeckListRecyclerViewAdapter(requireActivity(), deckViewModel.getDeckViews(), requireActivity())
        deckRecyclerViewAdapter.onDeckClicked = onDeckSelected
        binding.deckRecyclerView.adapter = deckRecyclerViewAdapter
        binding.deckRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}