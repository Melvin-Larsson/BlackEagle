package com.inglarna.blackeagle.ui.decklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.cardlist.CardListActivity
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : SingleFragmentNavMenuActivity(), MainFragment.DeckSelectedCallback{

    private var deleteButton: MenuItem? = null
    private val deckViewModel by viewModels<DeckViewModel>()

    override fun createFragment() = MainFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.deck_header)
    }
    private fun startCardActivity(deck: Deck) {
        startActivity(CardListActivity.newIntent(this, deck.id))
    }

    override fun onDeckSelected(deck: Deck) {
        startCardActivity(deck)
    }


}