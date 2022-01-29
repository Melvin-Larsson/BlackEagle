package com.inglarna.blackeagle.ui.convertnumber

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.editnumbers.EditNumbersActivity
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

class NumbersActivity: SingleFragmentNavMenuActivity() {

    override fun createFragment(): Fragment = NumbersFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.convert_numbers_header)
    }

}