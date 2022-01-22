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
import com.anychart.graphics.vector.Stroke;
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.inglarna.blackeagle.databinding.FragmentSettingsBinding
import com.inglarna.blackeagle.databinding.FragmentStatsBinding

import android.R
import android.os.Build
import android.util.Log
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class StatsFragment : Fragment() {
        lateinit var binding : FragmentStatsBinding

        companion object {
            fun newInstance() = StatsFragment()
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            binding = FragmentStatsBinding.inflate(inflater, container, false)
            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anyChartView: AnyChartView = binding.barChart

        val cartesian = AnyChart.column()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("MM-dd")
            var answer: String =  current.format(formatter)
            Log.d("answer",answer)
        } else {
            var date = Date()
            val formatter = SimpleDateFormat("MM-dd")
            val answer: String = formatter.format(date)
            Log.d("answer",answer)
        }

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("hej", 80540))
        data.add(ValueDataEntry("01-22", 94190))
        data.add(ValueDataEntry("day 3", 102610))
        data.add(ValueDataEntry("gj", 110430))
        data.add(ValueDataEntry("Lip", 128000))
        data.add(ValueDataEntry("Nail", 143760))
        data.add(ValueDataEntry("Eyeb", 170670))

        val column: Column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title("studies during the week")

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Product")
        cartesian.yAxis(0).title("Revenue")

        anyChartView.setChart(cartesian)
    }
}