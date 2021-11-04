package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.inglarna.blackeagle.databinding.FragmentCardListBinding

class CardListFragment : Fragment() {
    lateinit var binding : FragmentCardListBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerViewCard.adapter = CardListRecyclerViewAdapter()
        binding.recyclerViewCard.layoutManager = LinearLayoutManager(requireContext())
    }
}