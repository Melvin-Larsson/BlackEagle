package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    lateinit var binding : FragmentMainBinding

    companion object{
        fun newInstance() = MainFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}