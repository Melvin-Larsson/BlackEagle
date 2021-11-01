package com.inglarna.blackeagle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentConvertNumbersBinding

class ConvertNumbersFragment: Fragment() {
    lateinit var binding: FragmentConvertNumbersBinding

    companion object{
        fun newInstance() = ConvertNumbersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConvertNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }
}