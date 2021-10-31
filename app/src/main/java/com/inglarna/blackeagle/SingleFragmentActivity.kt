package com.inglarna.blackeagle

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.ActivityFragmentBinding

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
                R.id.yourDecks -> Toast.makeText(applicationContext,
                    "you are kind", Toast.LENGTH_SHORT).show()
                R.id.convertNumbers -> Toast.makeText(applicationContext,
                    "you are really good looking today", Toast.LENGTH_SHORT).show()
                R.id.settings -> Toast.makeText(applicationContext,
                    "you are smart", Toast.LENGTH_SHORT).show()
                R.id.stats -> Toast.makeText(applicationContext,
                    "you are brilliant", Toast.LENGTH_SHORT).show()
                R.id.about -> Toast.makeText(applicationContext,
                    "you are thoughtful", Toast.LENGTH_SHORT).show()
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

    abstract fun createFragment() : Fragment

}