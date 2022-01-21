package com.inglarna.blackeagle.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.ui.convertnumber.NumbersActivity
import com.inglarna.blackeagle.ui.decklist.MainActivity
import com.inglarna.blackeagle.ui.settings.SettingsActivity
import com.inglarna.blackeagle.ui.stats.StatsActivity

//Test
abstract class SingleFragmentNavMenuActivity: SingleFragmentActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Navigation menu
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            //when clicking in the hamburgermenu
            when(it.itemId) {
                R.id.yourDecks -> startDecks()
                R.id.convertNumbers -> startConvertNumbers()
                R.id.settings -> startSettings()
                R.id.stats -> startStats()
            }
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    //button functions
    private fun startDecks(){
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun startConvertNumbers(){
        startActivity(Intent(this, NumbersActivity::class.java))
    }
    private fun startSettings(){
        startActivity(Intent(this, SettingsActivity::class.java))
    }
    private fun startStats(){
        startActivity(Intent(this, StatsActivity::class.java))
    }
}