package com.inglarna.blackeagle.ui.card

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.SingleFragmentActivity

class CardActivity : SingleFragmentActivity() {

     private var deckId: Long = -1
     private var cardId: Long = -1

     companion object{
          private const val DECK_ID = "deckId"
          private const val CARD_ID = "cardId"
          fun newIntent(context: Context, deckId: Long?) : Intent{
               val intent = Intent(context, CardActivity::class.java)
               intent.putExtra(DECK_ID, deckId)
               return  intent
          }
          fun newIntent(context: Context, deckId: Long?, cardId: Long?) : Intent{
               val intent = Intent(context, CardActivity::class.java)
               intent.putExtra(DECK_ID, deckId)
               intent.putExtra(CARD_ID, cardId)
               return  intent
          }
     }

     override fun createFragment(): Fragment = CardFragment.newInstance(deckId, cardId)

     override fun onCreate(savedInstanceState: Bundle?) {
          deckId = intent.getLongExtra(DECK_ID, -1)
          cardId = intent.getLongExtra(CARD_ID, -1)
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