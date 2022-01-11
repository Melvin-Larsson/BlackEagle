package com.inglarna.blackeagle.ui.folderpicker

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class FolderPickerActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = FolderPickerFragment.newInstance()
    companion object{
        fun newIntent(context: Context) = Intent(context, FolderPickerActivity::class.java)
    }
}