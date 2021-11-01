package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.AboutFragment

class AboutActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = AboutFragment.newInstance()
}