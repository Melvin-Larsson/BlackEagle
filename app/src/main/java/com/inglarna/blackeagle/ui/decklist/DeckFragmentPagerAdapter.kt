package com.inglarna.blackeagle.ui.decklist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.inglarna.blackeagle.ui.decklist.decklist.DeckListFragment
import com.inglarna.blackeagle.ui.decklist.folderlist.FolderListFragment

class DeckFragmentPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        if(position == 1){
            return FolderListFragment.newInstance()
        }
        return DeckListFragment.newInstance(position)
    }
    companion object{
        const val ARG_OBJECT = "object"
    }
}

