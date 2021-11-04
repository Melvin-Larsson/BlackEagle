package com.inglarna.blackeagle

import android.content.Intent
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.ui.MainFragment

class MainActivity: SingleFragmentActivity() {
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