package com.inglarna.blackeagle.ui.decklist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.SingleFragmentNavMenuActivity
import com.inglarna.blackeagle.model.Deck
import com.inglarna.blackeagle.model.DeckWithCards
import com.inglarna.blackeagle.ui.cardlist.CardListActivity
import com.inglarna.blackeagle.ui.decklist.decklist.DeckListFragment

class MainActivity : SingleFragmentNavMenuActivity(), DeckListFragment.DeckSelectedCallback, EditableListActivity{
    //Toolbar buttons
    private lateinit var deleteButton: MenuItem
    private lateinit var selectAllButton: MenuItem
    private lateinit var closeSelectButton: MenuItem
    private lateinit var importButton: MenuItem

    override fun createFragment() = MainFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.deck_header)
    }
    private fun startCardActivity(deck: Deck) {
        startActivity(CardListActivity.newIntent(this, deck.deckId))
    }

    override fun onDeckSelected(deck: Deck) {
        startCardActivity(deck)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.deck_list_menu, menu)
        selectAllButton = menu.findItem(R.id.selectAll)
        deleteButton = menu.findItem((R.id.delete))
        importButton = menu.findItem(R.id.importDeck)
        closeSelectButton = menu.findItem(R.id.closeSelect)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId) {
            R.id.closeSelect -> setSelectVisibility(false)
            R.id.importDeck -> startFileExplorerForResult.launch("*/*")
        }
        return false
    }
    private val startFileExplorerForResult = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null){
            DeckWithCards.import(this, uri)
        }
    }

    override fun setSelectVisibility(showSelect: Boolean) {
        //visibility of toolbar and checkbox
        deleteButton.isVisible = showSelect
        selectAllButton.isVisible = showSelect
        closeSelectButton.isVisible = showSelect
        importButton.isVisible = !showSelect
    }
}