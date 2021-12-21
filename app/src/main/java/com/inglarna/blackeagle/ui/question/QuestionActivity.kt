package com.inglarna.blackeagle.ui.question

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class QuestionActivity : SingleFragmentActivity() {
    var deckId : Long = -1

    companion object{
        private const val DECK_ID = "deckId"
        fun newIntent(context: Context, deckId: Long?) : Intent {
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra(DECK_ID, deckId)
            return  intent
        }
    }

    override fun createFragment() =  QuestionFragment.newInstance(deckId)

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
