package com.inglarna.blackeagle.ui.cardlist

import android.content.Context
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
    private val deckViewModel by viewModels<DeckViewModel>()
    private var favoriteButton: MenuItem? = null

    companion object{
        const val DECK_ID = "deckId"
        fun newIntent(context: Context, deckId : Long?) : Intent{
            val intent = Intent(context, CardListActivity::class.java)
            intent.putExtra(DECK_ID, deckId)
            return intent
        }
    }
    override fun createFragment(): Fragment{
        val fragment = CardListFragment()
        fragment.onAddCardClicked = {
            startActivity(AddCardActivity.newIntent(this, id))
        }
        return fragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getLongExtra(DECK_ID, -1)
    }

    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.card_list_menu, menu)
        favoriteButton = menu?.findItem(R.id.favoriteStudy)
        deckViewModel.getDeckViews()?.observe(this,{decks->
            for(deck in decks){
                if (deck.id == id){
                    setFavoriteIcon(deck.favorite)
                }
            }
        })
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
            favoriteButton?.setIcon(R.drawable.ic_favorite_study)
        }else{
            favoriteButton?.setIcon(R.drawable.ic_favorite_study_border)
        }
    }
}