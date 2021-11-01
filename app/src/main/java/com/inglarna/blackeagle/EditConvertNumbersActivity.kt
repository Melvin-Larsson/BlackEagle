package com.inglarna.blackeagle

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.EditConvertNumbersFragment

class EditConvertNumbersActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val fragment = EditConvertNumbersFragment.newInstance()
        return fragment
    }
}