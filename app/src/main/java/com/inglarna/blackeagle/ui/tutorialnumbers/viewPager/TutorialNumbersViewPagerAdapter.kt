package com.inglarna.blackeagle.ui.tutorialnumbers.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class TutorialNumbersViewPagerAdapter(list: ArrayList<Fragment>, fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}