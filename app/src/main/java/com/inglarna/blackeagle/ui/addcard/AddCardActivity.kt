package com.inglarna.blackeagle.ui.addcard

import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class AddCardActivity : SingleFragmentActivity() {
     override fun createFragment(): Fragment = AddCardFragment.newInstance()
}