package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.inglarna.blackeagle.databinding.FragmentDeckPagerBinding
import com.inglarna.blackeagle.databinding.FragmentEditCardBinding
import com.inglarna.blackeagle.databinding.FragmentQuestionBinding

class MainFragment : Fragment() {

    lateinit var binding : FragmentDeckPagerBinding
    private lateinit var deckFragmentPagerAdapter: DeckFragmentPagerAdapter

    companion object{
        fun newInstance() = MainFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentDeckPagerBinding.inflate(inflater, container, false)
        return FragmentQuestionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.deckPager.adapter = DeckFragmentPagerAdapter(this)
        TabLayoutMediator(binding.deckTabLayout, binding.deckPager){tab, position ->
            tab.text = "Position: " + position
        }.attach()
    }
}