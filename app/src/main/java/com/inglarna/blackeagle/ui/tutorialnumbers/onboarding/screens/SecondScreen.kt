package com.jovanovic.stefan.mytestapp.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.FragmentTutorialNumbers2Binding

class SecondScreen : Fragment() {

    lateinit var binding: FragmentTutorialNumbers2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_tutorial_numbers_2, container, false)
        return view
    }

}