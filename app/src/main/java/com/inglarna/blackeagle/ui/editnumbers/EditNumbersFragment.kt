package com.inglarna.blackeagle.ui.editnumbers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentEditNumbersBinding

class EditNumbersFragment: Fragment() {
    lateinit var binding: FragmentEditNumbersBinding

    companion object{
        fun newInstance() = EditNumbersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

}