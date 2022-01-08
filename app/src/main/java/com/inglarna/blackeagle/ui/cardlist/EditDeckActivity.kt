package com.inglarna.blackeagle.ui.cardlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class EditDeckActivity: SingleFragmentActivity() {
    private var deckId: Long = -1

    companion object{
        private const val DECK_ID = "deckId"
        fun newIntent(context: Context, deckId: Long?) : Intent{
            val intent = Intent(context, EditDeckActivity::class.java)
            intent.putExtra(DECK_ID, deckId)
            return intent
        }
    }
    override fun createFragment(): Fragment = EditDeckFragment.newInstance(deckId)

    override fun onCreate(savedInstanceState: Bundle?) {
        deckId = intent.getLongExtra(DECK_ID, -1)
        super.onCreate(savedInstanceState)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return true
    }
}