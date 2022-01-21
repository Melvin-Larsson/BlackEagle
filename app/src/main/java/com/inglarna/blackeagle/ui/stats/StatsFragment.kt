package com.inglarna.blackeagle.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {
        lateinit var binding : FragmentStatsBinding

        companion object {
            fun newInstance() = StatsFragment()
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            binding = FragmentStatsBinding.inflate(inflater, container, false)
            return binding.root
        }

}