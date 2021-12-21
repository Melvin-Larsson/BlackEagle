package com.inglarna.blackeagle.ui.cardlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.model.Card
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.addcard.AddCardActivity
import com.inglarna.blackeagle.viewmodel.DeckViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardListActivity : SingleFragmentActivity(), CardListFragment.EditCardSelectedCallBack {

    var id: Long = -1

    companion object{
        private const val DECK_ID = "deckId"
        fun newIntent(context: Context, deckId : Long?) : Intent{
            val intent = Intent(context, CardListActivity::class.java)
            intent.putExtra(DECK_ID, deckId)
            return intent
        }
    }
    override fun createFragment(): Fragment{
        val fragment = CardListFragment.newInstance(id)
        fragment.onAddCardClicked = {
            startActivity(AddCardActivity.newIntent(this, id))
        }
        return fragment
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        id = intent.getLongExtra(DECK_ID, -1)
        //super.onCreate is after since it will call createFragment, createFragment needs a valid id
        super.onCreate(savedInstanceState)
    }

    override fun onEditCardSelected(card: Card) {

    }
}