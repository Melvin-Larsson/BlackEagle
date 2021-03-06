package com.inglarna.blackeagle.ui.cardlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class CardListActivity : SingleFragmentActivity() {

    var id: Long = -1

    companion object{
        private const val DECK_ID = "deckId"
        fun newIntent(context: Context, deckId : Long?) : Intent{
            val intent = Intent(context, CardListActivity::class.java)
            intent.putExtra(DECK_ID, deckId)
            return intent
        }
    }
    override fun createFragment(): Fragment = CardListFragment.newInstance(id)
    override fun onCreate(savedInstanceState: Bundle?) {
        id = intent.getLongExtra(DECK_ID, -1)
        //super.onCreate is after since it will call createFragment, createFragment needs a valid id
        super.onCreate(savedInstanceState)
    }
}