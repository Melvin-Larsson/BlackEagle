package com.inglarna.blackeagle.ui.editnumbers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.databinding.FragmentEditNumbersBinding
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
        binding.textInputSearch?.addTextChangedListener{
            wordNumberRecyclerViewAdapter.liveData = wordNumberViewModel.getWords(binding.textInputSearch?.text.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wordNumberRecyclerViewAdapter = EditNumbersListRecyclerViewAdapter(requireContext(), wordNumberViewModel.getNumberViews(), this)
        binding.editNumbersRecyclerview.adapter = wordNumberRecyclerViewAdapter
        binding.editNumbersRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}