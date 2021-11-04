package com.inglarna.blackeagle.ui.editcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentEditCardBinding

class EditCardFragment: Fragment() {
    lateinit var binding: FragmentEditCardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditCardBinding.inflate(inflater, container, false)
        return binding.root
    }
}