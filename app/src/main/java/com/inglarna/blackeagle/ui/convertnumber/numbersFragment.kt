package com.inglarna.blackeagle.ui.convertnumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentNumbersBinding

class numbersFragment: Fragment() {

    lateinit var binding: FragmentNumbersBinding
    lateinit var callbacks: Callbacks

    interface Callbacks{
        fun onEditButtonPressed() {
        }
    }

    companion object{
        fun newInstance() = numbersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNumbersBinding.inflate(inflater, container, false)
        binding.editNumbersButton.setOnClickListener(){
            callbacks.onEditButtonPressed()
        }
        return binding.root
    }
}