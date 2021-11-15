package com.inglarna.blackeagle.ui.cardlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.BlackEagleDatabase_Impl
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.addcard.AddCardActivity
import com.inglarna.blackeagle.ui.decklist.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardListActivity : SingleFragmentActivity() {

    var id: Long = -1

    override fun createFragment(): Fragment{
        val fragment = CardListFragment()
        fragment.onAddCardClicked = {
            startActivity(Intent(this, AddCardActivity::class.java))
        }
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getLongExtra(MainActivity.DECK_ID, -1)
    }

    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.card_list_menu, menu)
        return true
    }

    //when clicking in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
            R.id.startStudy -> startStudy()
            R.id.favoriteStudy -> favorites(item)
        }
        return true
    }

    private fun startStudy(){
        Toast.makeText(this, "start study", Toast.LENGTH_SHORT).show()
    }
    private fun favorites(item: MenuItem){
        var deckDao = BlackEagleDatabase.getInstance(this).deckDao()
        item.setIcon(R.drawable.ic_favorite_study)
        GlobalScope.launch {
            var deck = deckDao.getDeck(id)
            deck.favorite = !deck.favorite
            deckDao.updateDeck(deck)
        }

    }
}