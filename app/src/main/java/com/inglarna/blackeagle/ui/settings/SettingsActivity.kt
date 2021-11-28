package com.inglarna.blackeagle.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

class SettingsActivity: SingleFragmentNavMenuActivity() {
    override fun createFragment(): Fragment = SettingsFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.settings)
    }
}