package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.AboutFragment

//aboutActivity
class AboutActivity: SingleFragmentNavMenuActivity() {
    override fun createFragment(): Fragment = AboutFragment.newInstance()
}