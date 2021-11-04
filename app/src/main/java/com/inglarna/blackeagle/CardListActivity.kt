package com.inglarna.blackeagle

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.ui.CardListFragment

class CardListActivity : HamburgermenuActivity() {
    override fun createFragment(): Fragment = CardListFragment()

    //card list menu is used as a toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.card_list_menu, menu)
        return true
    }

    //when clicking in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.startStudy -> Toast.makeText(this, "you clicked on start study", Toast.LENGTH_SHORT).show()
            R.id.favoriteStudy -> Toast.makeText(this, "I love you", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}