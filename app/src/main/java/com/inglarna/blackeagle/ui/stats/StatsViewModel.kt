package com.inglarna.blackeagle.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.inglarna.blackeagle.model.Stat
import com.inglarna.blackeagle.repository.StatRepo
import getCurrentDay

class StatsViewModel(application: Application) : AndroidViewModel(application) {
    private val statRepo = StatRepo(application)

    fun getLastWeekStats(): LiveData<List<Stat>>{
        val maxDate = getCurrentDay()
        val minDate = maxDate - 6
        return statRepo.getStats(minDate, maxDate).map { stats ->
            //Add an empty stat for every day in the interval maxDate-minDate that does not have a stat in the database
            val fullStatList = mutableListOf<Stat>()
            var index = 0
            for (date in minDate..maxDate) {
                if(index >= stats.size || stats[index].date != date){
                    fullStatList.add(Stat(date))
                }else{
                    fullStatList.add(stats[index])
                    index++
                }
            }
            fullStatList
        }
    }

}