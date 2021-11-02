package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SettingsFragment

class SettingsActivity: HamburgermenuActivity() {
    override fun createFragment(): Fragment = SettingsFragment.newInstance()

}