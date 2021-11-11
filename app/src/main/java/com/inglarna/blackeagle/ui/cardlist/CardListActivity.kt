package com.inglarna.blackeagle.ui.cardlist

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.SingleFragmentActivity
import com.inglarna.blackeagle.ui.addcard.AddCardActivity

class CardListActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment{
        val fragment = CardListFragment()
        fragment.onAddCardClicked = {
            startActivity(Intent(this, AddCardActivity::class.java))
            Log.d("INGVAR", "onAddCardClicked")
        }
        return fragment
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
            R.id.startStudy -> Toast.makeText(this, "you clicked on start study", Toast.LENGTH_SHORT).show()
            R.id.favoriteStudy -> Toast.makeText(this, "I love you", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}