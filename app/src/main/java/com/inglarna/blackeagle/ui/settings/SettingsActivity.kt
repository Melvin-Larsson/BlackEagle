package com.inglarna.blackeagle.ui.settings

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

class SettingsActivity: SingleFragmentNavMenuActivity() {
    override fun createFragment(): Fragment = SettingsFragment.newInstance()

}