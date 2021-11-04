package com.inglarna.blackeagle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.ConvertNumbersFragment

class ConvertNumbersActivity: HamburgermenuActivity(), ConvertNumbersFragment.Callbacks {

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