package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.MainFragment

class MainActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment = MainFragment()
}