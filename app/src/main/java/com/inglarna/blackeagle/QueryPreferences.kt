package com.inglarna.blackeagle

import android.content.Context
import androidx.preference.PreferenceManager

class QueryPreferences {
    companion object{
        private const val DEFAULT_DAILY_REPETITION_GOAL = 40 //FIXME: This does not match with the default repetition goal showed in settings

        fun getDailyRepetitionGoal(context: Context): Int{
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("dailyRepetitionGoal", DEFAULT_DAILY_REPETITION_GOAL.toString())!!.toInt() //TextInputPreferences are stored as a string even though input type is number
        }

        fun isDarkTheme(context: Context): Boolean{
            return  PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean("darkMode", false)
        }
    }
}