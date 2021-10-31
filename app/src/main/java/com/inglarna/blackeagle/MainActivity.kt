package com.inglarna.blackeagle

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.inglarna.blackeagle.databinding.ActivityFragmentBinding
import com.inglarna.blackeagle.databinding.FragmentMainBinding
import com.inglarna.blackeagle.ui.MainFragment

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = FragmentMainBinding.inflate(layoutInflater)
            setContentView(binding.root)


        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.miItem -> Toast.makeText(applicationContext,
                    "you are gay", Toast.LENGTH_SHORT).show()
                R.id.miItem2 -> Toast.makeText(applicationContext,
                    "you are gay2", Toast.LENGTH_SHORT).show()
                R.id.miItem3 -> Toast.makeText(applicationContext,
                    "you are gay3", Toast.LENGTH_SHORT).show()
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
}