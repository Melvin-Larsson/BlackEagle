package com.inglarna.blackeagle.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.inglarna.blackeagle.model.Deck

class DeckFragmentPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    lateinit var onDeckSelected: ((Deck) -> Unit)

    override fun createFragment(position: Int): Fragment {
        val fragment = DeckListFragment.newInstance()
        fragment.onDeckSelected = onDeckSelected
        /*fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }*/
        return fragment
    }
    companion object{
        public const val ARG_OBJECT = "object"
    }
}


