package com.inglarna.blackeagle.ui.convertnumber

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.editnumbers.EditNumbersActivity
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

class numbersActivity: SingleFragmentNavMenuActivity(), numbersFragment.Callbacks {

    override fun createFragment(): Fragment {
        val fragment = numbersFragment.newInstance()
        fragment.callbacks = this
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.convert_numbers_header)
    }
    //when clicking edit button
    override fun onEditButtonPressed() {
        val intentConvertNumbers = Intent(this, EditNumbersActivity::class.java)
        startActivity(intentConvertNumbers)
    }

}