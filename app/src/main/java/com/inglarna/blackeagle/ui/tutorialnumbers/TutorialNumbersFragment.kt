package com.inglarna.blackeagle.ui.tutorialnumbers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentTutorialNumbesBinding

class TutorialNumbersFragment: Fragment() {
    lateinit var binding: FragmentTutorialNumbesBinding

    companion object{
        fun newInstance() = TutorialNumbersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTutorialNumbesBinding.inflate(inflater, container, false)
        return binding.root
    }
}