package com.inglarna.blackeagle.ui.decklist

import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.cardlist.CardListActivity
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

    fun startCardActivity(deck: Deck) {
        val intent = Intent(this, CardListActivity::class.java)
        startActivity(intent)
    }
}