package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inglarna.blackeagle.databinding.FragmentDeckPagerBinding
import com.inglarna.blackeagle.model.Deck

class MainFragment : Fragment() {

    lateinit var binding : FragmentDeckPagerBinding
    lateinit var onDeckSelected: ((Deck) -> Unit)

    companion object{
        fun newInstance() = MainFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentDeckPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = DeckFragmentPagerAdapter(this)
        adapter.onDeckSelected = onDeckSelected

        binding.deckPager.adapter = adapter
        TabLayoutMediator(binding.deckTabLayout, binding.deckPager){tab, position ->
            tab.text = "Position: " + position
        }.attach()
    }
}