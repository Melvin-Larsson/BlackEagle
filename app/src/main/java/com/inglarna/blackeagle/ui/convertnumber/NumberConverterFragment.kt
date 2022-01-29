package com.inglarna.blackeagle.ui.convertnumber

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.inglarna.blackeagle.databinding.FragmentNumberConverterBinding

class NumberConverterFragment : Fragment() {
    private lateinit var binding: FragmentNumberConverterBinding
    private val viewModel by viewModels<NumbersViewModel>()

    companion object{
        private const val TAG = "NumbersConverterFragment"
        fun newInstance() = NumberConverterFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNumberConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}