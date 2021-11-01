package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.ConvertNumbersFragment

class ConvertNumbersActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = ConvertNumbersFragment.newInstance()
}