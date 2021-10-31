package com.inglarna.blackeagle

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.inglarna.blackeagle.databinding.ActivityMainBinding
import com.inglarna.blackeagle.ui.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.deck_container, mainFragment)
                .commitNow()
        }
    }
}