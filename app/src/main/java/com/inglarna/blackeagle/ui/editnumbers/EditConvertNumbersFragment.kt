package com.inglarna.blackeagle.ui.editnumbers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentEditConvertNumbersBinding

class EditConvertNumbersFragment: Fragment() {
    lateinit var binding: FragmentEditConvertNumbersBinding

    companion object{
        fun newInstance() = EditConvertNumbersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditConvertNumbersBinding.inflate(inflater, container, false)
        return binding.root
    }

}