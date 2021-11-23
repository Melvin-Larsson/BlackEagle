package com.inglarna.blackeagle.ui.cardlist

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
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.addcard.AddCardActivity
import com.inglarna.blackeagle.ui.decklist.MainActivity
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardListActivity : SingleFragmentActivity() {

    var id: Long = -1
    var favorite = false
    private val deckViewModel by viewModels<DeckViewModel>()
    private var favoriteButton: MenuItem? = null

    companion object{
        const val DECK_ID = "deckId"
    }
    override fun createFragment(): Fragment{
        val fragment = CardListFragment()
        fragment.onAddCardClicked = {
            val intent = Intent(Intent(this, AddCardActivity::class.java))
            intent.putExtra(DECK_ID, id)
            startActivity(intent)
        }
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getLongExtra(MainActivity.DECK_ID, -1)
        deckViewModel.getDeckViews()?.observe(this,{decks->
            Log.d("nånting", "" + decks.size)
            for(deck in decks){
                if (deck.id == id){
                    setFavoriteIcon(deck.favorite)
                }
            }
        })
    }

    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.card_list_menu, menu)
        favoriteButton = menu?.findItem(R.id.favoriteStudy)
        favorite = intent.getBooleanExtra(MainActivity.DECK_FAVORITE, false)
        setFavoriteIcon(favorite)
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
            R.id.favoriteStudy -> favorites()
        }
        return true
    }

    private fun startStudy(){
        Toast.makeText(this, "start study", Toast.LENGTH_SHORT).show()
    }
    private fun favorites() {
        val deckDao = BlackEagleDatabase.getInstance(this).deckDao()

        GlobalScope.launch {
            val deck = deckDao.getDeck(id)
            deck.favorite = !deck.favorite
            deckDao.updateDeck(deck)
        }
    }
    private fun setFavoriteIcon(favorite: Boolean){
        if (favorite){
            favoriteButton?.setIcon(R.drawable.ic_favorite_study_border)
        }else{
            favoriteButton?.setIcon(R.drawable.ic_favorite_study)
        }
    }
}