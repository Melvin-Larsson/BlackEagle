package com.inglarna.blackeagle.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

//aboutActivity
class AboutActivity: SingleFragmentNavMenuActivity() {
    override fun createFragment(): Fragment = AboutFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.about)
    }
}