package com.inglarna.blackeagle.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

class StatsActivity: SingleFragmentNavMenuActivity() {
    override fun createFragment(): Fragment = StatsFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar
    }
}