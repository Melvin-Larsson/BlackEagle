package com.inglarna.blackeagle

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.EditConvertNumbersFragment

class EditConvertNumbersActivity: HamburgermenuActivity() {
    override fun createFragment(): Fragment {
        val fragment = EditConvertNumbersFragment.newInstance()
        return fragment
    }

}