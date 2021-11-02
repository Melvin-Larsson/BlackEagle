package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.AboutFragment

class AboutActivity: HamburgermenuActivity() {
    override fun createFragment(): Fragment = AboutFragment.newInstance()
}