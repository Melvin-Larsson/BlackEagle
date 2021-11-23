package com.inglarna.blackeagle.ui.decklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class MainActivity : SingleFragmentNavMenuActivity() {

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
    fun startCardActivity(deck: DeckViewModel.DeckView) {
        val intent = Intent(this, CardListActivity::class.java)
        intent.putExtra(DECK_ID, deck.id)
        intent.putExtra(DECK_FAVORITE, deck.favorite)
        startActivity(intent)
    }
}