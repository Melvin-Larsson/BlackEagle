package com.inglarna.blackeagle.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.inglarna.blackeagle.model.Stat

@Dao
interface StatDao {

    @Query("SELECT * FROM Stat WHERE date = :date")
    fun getLiveStat(date: Long): LiveData<Stat>

    @Query("SELECT * FROM Stat WHERE date = :date")
    fun getStat(date: Long): Stat?

    @Query("SELECT * FROM Stat WHERE date >= :minDate AND date <= :maxDate")
    fun getStats(minDate: Long, maxDate: Long): LiveData<List<Stat>>

    @Query("UPDATE Stat SET studies = studies + 1 WHERE date = :date")
    fun incrementStat(date: Long)

    @Insert(onConflict = REPLACE)
    fun insertStat(stat: Stat)

}