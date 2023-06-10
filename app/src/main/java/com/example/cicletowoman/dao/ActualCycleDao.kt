package com.example.cicletowoman.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cicletowoman.entities.ActualCycle

@Dao
interface ActualCycleDao {
    @Query("SELECT * FROM actualcycle WHERE uid = :uid")
    fun findByRunning(uid: String): ActualCycle

    @Insert
    fun insert(vararg user: ActualCycle)

    @Query("DELETE FROM actualcycle WHERE uid = :uid")
    fun delete(uid: String)
}