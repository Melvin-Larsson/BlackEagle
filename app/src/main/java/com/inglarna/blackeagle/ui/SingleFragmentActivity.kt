package com.inglarna.blackeagle.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.inglarna.blackeagle.QueryPreferences
import com.inglarna.blackeagle.R
import com.inglarna.blackeagle.databinding.ActivityFragmentBinding
import com.inglarna.blackeagle.db.BlackEagleDatabase

abstract class SingleFragmentActivity : AppCompatActivity(){

    lateinit var binding: ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set default word number language FIXME: Odd plays to this this? will do this every time a new activity is started
        QueryPreferences.setDefaultNumberWordsLanguage(this)

        //Theme
        refreshTheme()

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

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun refreshTheme(){
        if(QueryPreferences.isDarkTheme(this)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}