package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding

    companion object{
        fun newInstance() = MainFragment()
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        Log.d("MainFragment", "Created other shit")
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}