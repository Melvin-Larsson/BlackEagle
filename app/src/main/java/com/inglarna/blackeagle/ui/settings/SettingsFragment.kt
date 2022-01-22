package com.inglarna.blackeagle.ui.settings

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.inglarna.blackeagle.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    lateinit var barList:ArrayList<BarEntry>
    lateinit var binding : FragmentSettingsBinding
    lateinit var barDataSet: BarDataSet
    lateinit var barData: BarData

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barList = ArrayList()
        barList.add(BarEntry(10f, 100f))
        barList.add(BarEntry(10f, 100f))
        barList.add(BarEntry(10f, 100f))
        barList.add(BarEntry(10f, 100f))
        barList.add(BarEntry(10f, 100f))
        barList.add(BarEntry(10f, 100f))
        barList.add(BarEntry(10f, 100f))
        barDataSet = BarDataSet(barList, "studies")
        barData = BarData(barDataSet)
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 15f
    }
}