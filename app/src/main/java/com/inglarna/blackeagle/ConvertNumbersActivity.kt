package com.inglarna.blackeagle

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.ConvertNumbersFragment

class ConvertNumbersActivity: HamburgermenuActivity(), ConvertNumbersFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun createFragment(): Fragment {
        val fragment = ConvertNumbersFragment.newInstance()
        fragment.callbacks = this
        return fragment
    }

    override fun onEditButtonPressed() {
        val intentConvertNumbers = Intent(this, EditConvertNumbersActivity::class.java)
        startActivity(intentConvertNumbers)
    }

}