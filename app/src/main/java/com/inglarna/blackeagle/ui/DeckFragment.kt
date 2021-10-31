package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.inglarna.blackeagle.databinding.FragmentMainBinding
import com.inglarna.blackeagle.databinding.FragmentPagerBinding

class DeckFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding

    companion object{
        fun newInstance() = DeckFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}