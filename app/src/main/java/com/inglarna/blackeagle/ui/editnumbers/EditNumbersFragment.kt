package com.inglarna.blackeagle.ui.editnumbers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.databinding.FragmentEditNumbersBinding
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.model.WordNumber
import com.inglarna.blackeagle.ui.convertnumber.numbersFragment
import com.inglarna.blackeagle.ui.decklist.DeckListRecyclerViewAdapter
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import com.inglarna.blackeagle.viewmodel.wordNumberViewModel

class EditNumbersFragment: Fragment() {
    private lateinit var binding: FragmentEditNumbersBinding
    private lateinit var wordNumberRecyclerViewAdapter: EditNumbersListRecyclerViewAdapter
    private val wordNumberViewModel by viewModels<wordNumberViewModel>()

    companion object{
        fun newInstance() = EditNumbersFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordNumberRecyclerViewAdapter = EditNumbersListRecyclerViewAdapter(requireContext(), wordNumberViewModel.getNumberViews(), this)
        binding.editNumbersRecyclerview.adapter = wordNumberRecyclerViewAdapter
        binding.editNumbersRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}