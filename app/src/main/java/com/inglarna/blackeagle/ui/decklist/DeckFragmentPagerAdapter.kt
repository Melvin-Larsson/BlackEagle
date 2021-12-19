package com.inglarna.blackeagle.ui.decklist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.decklist.DeckListFragment
import com.inglarna.blackeagle.viewmodel.DeckViewModel

class DeckFragmentPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = DeckListFragment.newInstance(position)
        return fragment
    }
    companion object{
        const val ARG_OBJECT = "object"
    }
}

