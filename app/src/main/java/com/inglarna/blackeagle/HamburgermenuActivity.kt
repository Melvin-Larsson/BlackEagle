package com.inglarna.blackeagle

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.ActivityFragmentBinding

abstract class HamburgermenuActivity: AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Create view
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, createFragment())
                .commitNow()
        }

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
                R.id.about -> startAbout()
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
        startActivity(Intent(this, ConvertNumbersActivity::class.java))
    }
    private fun startSettings(){
        startActivity(Intent(this, SettingsActivity::class.java))
    }
    private fun startStats(){
        //startActivity(Intent(this, StatsActivity::class.java))
    }
    private fun startAbout(){
        startActivity(Intent(this, AboutActivity::class.java))
    }
    abstract fun createFragment() : Fragment
}