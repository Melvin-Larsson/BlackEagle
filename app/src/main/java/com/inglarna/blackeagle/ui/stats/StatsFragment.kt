package com.inglarna.blackeagle.ui.stats

import android.graphics.Color
import android.os.Bundle;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.LinearGauge.instantiate
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.inglarna.blackeagle.databinding.FragmentSettingsBinding
import com.inglarna.blackeagle.databinding.FragmentStatsBinding

import java.util.ArrayList;






class StatsFragment : Fragment() {
        lateinit var binding : FragmentStatsBinding
        lateinit var barList:ArrayList<BarEntry>
        lateinit var barDataSet: BarDataSet
        lateinit var barData: BarData

        companion object {
            fun newInstance() = StatsFragment()
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            binding = FragmentStatsBinding.inflate(inflater, container, false)
            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barList = ArrayList()
        barList.add(BarEntry(10f, 200f))
        barList.add(BarEntry(10f, 300f))
        barList.add(BarEntry(10f, 400f))
        barList.add(BarEntry(10f, 500f))
        barList.add(BarEntry(10f, 600f))
        barList.add(BarEntry(10f, 700f))
        barList.add(BarEntry(10f, 800f))
        val barDataSet = BarDataSet(barList, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        binding.barChart.data = data


        //hide grid lines
        binding.barChart.axisLeft.setDrawGridLines(false)
        binding.barChart.xAxis.setDrawGridLines(false)
        binding.barChart.xAxis.setDrawAxisLine(false)

        //remove right y-axis
        binding.barChart.axisRight.isEnabled = false

        //remove legend
        binding.barChart.legend.isEnabled = false


        //remove description label
        binding.barChart.description.isEnabled = false


        //add animation
        binding.barChart.animateY(3000)


        //draw chart
        binding.barChart.invalidate()
    }
}