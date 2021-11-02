package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.MainFragment

class MainActivity : HamburgermenuActivity() {
    override fun createFragment(): Fragment = MainFragment()
}