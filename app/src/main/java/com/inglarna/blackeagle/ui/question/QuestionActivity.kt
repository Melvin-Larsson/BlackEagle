package com.inglarna.blackeagle.ui.question

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class QuestionActivity : SingleFragmentActivity() {
    private var deckId : Long = -1
    private var forceStudy = false

    companion object{
        private const val DECK_ID = "deckId"
        private const val FORCE_STUDY = "forceStudy"
        fun newIntent(context: Context, deckId: Long?, forceStudy : Boolean = false) : Intent {
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra(DECK_ID, deckId)
            intent.putExtra(FORCE_STUDY, forceStudy)
            return  intent
        }
    }

    override fun createFragment() =  QuestionFragment.newInstance(deckId, forceStudy)

    override fun onCreate(savedInstanceState: Bundle?) {
        deckId = intent.getLongExtra(DECK_ID, -1)
        forceStudy = intent.getBooleanExtra(FORCE_STUDY, false)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return true
    }
}
