package com.inglarna.blackeagle.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.inglarna.blackeagle.db.BlackEagleDatabase
import com.inglarna.blackeagle.db.StatDao
import com.inglarna.blackeagle.model.Stat
import getCurrentDay

class StatRepo (context: Context){
    private val db = BlackEagleDatabase.getInstance(context)
    private val statDao: StatDao = db.statDao()

    fun getStat(date: Long): LiveData<Stat>{
        return statDao.getLiveStat(date)
    }
    fun getStats(minDate: Long, maxDate: Long): LiveData<List<Stat>>{
        return statDao.getStats(minDate, maxDate)
    }
    fun getLastStats(days: Int): LiveData<List<Stat>>{
        val maxDate = getCurrentDay()
        val minDate = maxDate - days;
        return statDao.getStats(minDate, maxDate)
    }
    fun insertStat(stat: Stat){
        statDao.insertStat(stat)
    }
    fun incrementCurrentStat(){
        var stat = statDao.getStat(getCurrentDay())
        if(stat == null){
            stat = Stat()
        }
        stat.studies++
        statDao.insertStat(stat)
    }
}