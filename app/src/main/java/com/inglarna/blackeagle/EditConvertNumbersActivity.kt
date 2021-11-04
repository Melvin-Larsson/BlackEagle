package com.inglarna.blackeagle

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.EditConvertNumbersFragment

class EditConvertNumbersActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment = EditConvertNumbersFragment()

    //customization for back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}