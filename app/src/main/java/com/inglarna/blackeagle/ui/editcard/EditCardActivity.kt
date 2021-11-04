package com.inglarna.blackeagle.ui.editcard

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.about.AboutFragment

class EditCardActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = AboutFragment.newInstance()
}