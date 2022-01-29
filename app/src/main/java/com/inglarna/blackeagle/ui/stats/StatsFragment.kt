package com.inglarna.blackeagle.ui.stats

import android.os.Bundle;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.inglarna.blackeagle.databinding.FragmentStatsBinding

import androidx.fragment.app.viewModels
import com.anychart.enums.*
import com.inglarna.blackeagle.R
import java.util.*


class StatsFragment : Fragment() {
    private lateinit var binding : FragmentStatsBinding
    private val viewModel by viewModels<StatsViewModel>()

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

        viewModel.getLastWeekStats().observe(viewLifecycleOwner){ stats ->
            val data: MutableList<DataEntry> = ArrayList()
            for(stat in stats){
                data.add(ValueDataEntry(stat.getFormattedDate(), stat.studies))
            }
            val column = cartesian.column(data)
            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)

            cartesian.animation(true)
            cartesian.title(getString(R.string.reviews_last_7))

            cartesian.yScale().minimum(0.0)

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title(getString(R.string.date))
            cartesian.yAxis(0).title(getString(R.string.reviews))

            anyChartView.setChart(cartesian)
        }


    }
}