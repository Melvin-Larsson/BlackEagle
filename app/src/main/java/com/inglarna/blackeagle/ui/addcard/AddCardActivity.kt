package com.inglarna.blackeagle.ui.addcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.cardlist.CardListActivity

class AddCardActivity : SingleFragmentActivity() {

     var id: Long = -1

     companion object{
          private const val DECK_ID = "deckId"
          fun newIntent(context: Context, deckId: Long?) : Intent{
               val intent = Intent(context, AddCardActivity::class.java)
               intent.putExtra(DECK_ID, deckId)
               return  intent
          }
     }

     override fun createFragment(): Fragment = AddCardFragment.newInstance(id)

     override fun onCreate(savedInstanceState: Bundle?) {
          id = intent.getLongExtra(DECK_ID, -1)
          super.onCreate(savedInstanceState)
          supportActionBar!!.setDisplayHomeAsUpEnabled(true)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
          if(item.itemId == android.R.id.home){
               finish()
          }
          return true
     }
}