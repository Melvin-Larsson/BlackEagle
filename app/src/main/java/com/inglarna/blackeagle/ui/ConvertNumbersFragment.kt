package com.inglarna.blackeagle.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.SettingsActivity
import com.inglarna.blackeagle.databinding.FragmentConvertNumbersBinding

class ConvertNumbersFragment: Fragment() {

    lateinit var binding: FragmentConvertNumbersBinding
    lateinit var callbacks: Callbacks

    interface Callbacks{
        fun onEditButtonPressed() {

        }
    }

    companion object{
        fun newInstance() = ConvertNumbersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentConvertNumbersBinding.inflate(inflater, container, false)
        binding.editConvertNumbersButton.setOnClickListener(){
            callbacks.onEditButtonPressed()
        }
        return binding.root
    }
}