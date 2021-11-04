package com.inglarna.blackeagle

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
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
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, createFragment())
                .commitNow()
        }
    }
    abstract fun createFragment(): Fragment
}