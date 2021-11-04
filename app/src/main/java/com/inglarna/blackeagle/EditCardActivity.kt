package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.AboutFragment

class EditCardActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = AboutFragment.newInstance()
}