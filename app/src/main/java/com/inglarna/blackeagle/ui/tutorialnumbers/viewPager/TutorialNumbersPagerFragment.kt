package com.inglarna.blackeagle.ui.tutorialnumbers.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentTutorialNumbesPagerBinding
import com.inglarna.blackeagle.ui.tutorialnumbers.viewPager.screens.FirstScreen
import com.jovanovic.stefan.mytestapp.onboarding.screens.SecondScreen


class TutorialNumbersPagerFragment : Fragment() {
    lateinit var binding: FragmentTutorialNumbesPagerBinding

    companion object{
        fun newInstance() = TutorialNumbersPagerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentTutorialNumbesPagerBinding.inflate(inflater, container, false)
        activity?.title = ""
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen()
        )

        val adapter = TutorialNumbersViewPagerAdapter(fragmentList, this)
        binding.viewPager.adapter = adapter

        val indicator = binding.indicator
        indicator.setViewPager(binding.viewPager)

        return binding.root
    }


}