package com.inglarna.blackeagle.ui.decklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.cardlist.CardListActivity
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : SingleFragmentNavMenuActivity() {
    private val deckViewModel by viewModels<DeckViewModel>()

    override fun createFragment(): Fragment{
        val fragment = MainFragment()
        fragment.onDeckSelected = {deck ->
            startCardActivity(deck)
        }
        return fragment
    }
    companion object{
        const val DECK_ID = "deckId"
        const val DECK_FAVORITE = "deckFavorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deckViewModel.getDeckViews()?.observe(this, {
            var deck = it?.let {
                Toast.makeText(this, "Hello " + it.size, Toast.LENGTH_LONG)
                Log.i("MainActivity", it.toString())
            }
        })
        GlobalScope.launch {
            var deck = Deck(1, "Hello", true)
            deckViewModel.addDeck(deck)
        }
    }
    fun startCardActivity(deck: Deck) {
        val intent = Intent(this, CardListActivity::class.java)
        intent.putExtra(DECK_ID, deck.id)
        intent.putExtra(DECK_FAVORITE, deck.favorite)
        startActivity(intent)
    }
}