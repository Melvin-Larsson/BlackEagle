package com.inglarna.blackeagle.ui.addcard

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.cardlist.CardListActivity

class AddCardActivity : SingleFragmentActivity() {

     var id: Long = -1

     override fun createFragment(): Fragment = AddCardFragment.newInstance(id)

     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          id = intent.getLongExtra(CardListActivity.DECK_ID, -1)
     }
}