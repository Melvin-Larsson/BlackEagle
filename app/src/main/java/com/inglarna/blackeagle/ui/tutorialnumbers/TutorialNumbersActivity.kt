package com.inglarna.blackeagle.ui.tutorialnumbers

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.tutorialnumbers.viewPager.TutorialNumbersPagerFragment

class TutorialNumbersActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = TutorialNumbersPagerFragment.newInstance()

    companion object{
        fun newIntent(context: Context) : Intent = Intent(context, TutorialNumbersActivity::class.java)
    }
}