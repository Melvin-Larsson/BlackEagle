package com.inglarna.blackeagle.ui.decklist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.decklist.DeckListFragment
import com.inglarna.blackeagle.viewmodel.DeckViewModel

class DeckFragmentPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    lateinit var onDeckSelected: ((Deck) -> Unit)

    override fun createFragment(position: Int): Fragment {
        val fragment = DeckListFragment.newInstance(position)
        fragment.onDeckSelected = onDeckSelected
        return fragment
    }
    companion object{
        public const val ARG_OBJECT = "object"
    }
}

