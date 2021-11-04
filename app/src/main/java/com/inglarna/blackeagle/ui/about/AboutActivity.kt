package com.inglarna.blackeagle.ui.about

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

//aboutActivity
class AboutActivity: SingleFragmentNavMenuActivity() {
    override fun createFragment(): Fragment = AboutFragment.newInstance()
}