package com.inglarna.blackeagle.ui.question

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class QuestionActivity : SingleFragmentActivity() {
    private var deckId : Long = -1
    private var cardsToRepeat : Int = -1

    companion object{
        private const val DECK_ID = "deckId"
        private const val CARDS_TO_REPEAT = "forceStudy"
        fun newIntent(context: Context, deckId: Long?, cardsToRepeat: Int = -1) : Intent {
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra(DECK_ID, deckId)
            intent.putExtra(CARDS_TO_REPEAT, cardsToRepeat)
            return  intent
        }
    }

    override fun createFragment() =  QuestionFragment.newInstance(deckId, cardsToRepeat)

    override fun onCreate(savedInstanceState: Bundle?) {
        deckId = intent.getLongExtra(DECK_ID, -1)
        cardsToRepeat = intent.getIntExtra(CARDS_TO_REPEAT, -1)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return true
    }
}
