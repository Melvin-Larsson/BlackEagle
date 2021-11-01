package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SettingsFragment

class SettingsActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = SettingsFragment.newInstance()

}