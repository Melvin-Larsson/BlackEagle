package com.inglarna.blackeagle.ui.editnumbers

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity

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