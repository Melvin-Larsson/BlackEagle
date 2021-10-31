package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentMainBinding
import com.inglarna.blackeagle.databinding.FragmentPagerBinding

class MainFragment : Fragment() {

    lateinit var binding : FragmentPagerBinding
    private lateinit var deckFragmentPagerAdapter: DeckFragmentPagerAdapter

    companion object{
        fun newInstance() = MainFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deckFragmentPagerAdapter = DeckFragmentPagerAdapter(this)
        binding.pager.adapter = deckFragmentPagerAdapter
    }
}