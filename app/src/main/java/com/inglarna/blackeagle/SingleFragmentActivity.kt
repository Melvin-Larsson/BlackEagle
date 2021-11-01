package com.inglarna.blackeagle

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.ActivityFragmentBinding
import com.inglarna.blackeagle.ui.MainFragment

abstract class SingleFragmentActivity : AppCompatActivity(){

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
        val intentDecks = Intent(this, MainActivity::class.java)
        startActivity(intentDecks)
    }
    private fun startConvertNumbers(){
        val intentConvert = Intent(this, ConvertNumbersActivity::class.java)
        startActivity(intentConvert)
    }
    private fun startSettings(){
        val intentSettings = Intent(this, SettingsActivity::class.java)
        startActivity(intentSettings)
    }
    private fun startStats(){
        //val intentStats = Intent(this, StatsActivity::class.java)
        //startActivity(intentStats)
    }
    private fun startAbout(){
        val intentAbout = Intent(this, AboutActivity::class.java)
        startActivity(intentAbout)
    }
    abstract fun createFragment() : Fragment


}