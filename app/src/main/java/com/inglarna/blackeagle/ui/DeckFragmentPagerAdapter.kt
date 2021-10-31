package com.inglarna.blackeagle.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
class DeckFragmentPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = DeckFragment.newInstance()
        /*fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position)
        }*/
        return fragment
    }
    companion object{
        public const val ARG_OBJECT = "object"
    }
}


