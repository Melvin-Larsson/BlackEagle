package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inglarna.blackeagle.databinding.FragmentDeckListBinding

class DeckListFragment : Fragment() {
    private lateinit var binding : FragmentDeckListBinding

    companion object{
        fun newInstance() = DeckListFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}