package com.inglarna.blackeagle.ui.convertnumber

import android.content.Intent
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.editnumbers.EditConvertNumbersActivity
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity

class ConvertNumbersActivity: SingleFragmentNavMenuActivity(), ConvertNumbersFragment.Callbacks {

    override fun createFragment(): Fragment {
        val fragment = ConvertNumbersFragment.newInstance()
        fragment.callbacks = this
        return fragment
    }

    //when clicking edit button
    override fun onEditButtonPressed() {
        val intentConvertNumbers = Intent(this, EditConvertNumbersActivity::class.java)
        startActivity(intentConvertNumbers)
    }

}