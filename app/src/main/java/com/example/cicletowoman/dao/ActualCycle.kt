package com.example.cicletowoman.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cicletowoman.entities.ActualCycle

@Dao
interface ActualCycleDao {
    @Query("SELECT * FROM actualcycle")
    fun getAll(): List<ActualCycle>

    @Query("SELECT * FROM actualcycle WHERE actual")
    fun findByRunning(): ActualCycle

    @Insert
    fun insert(vararg user: ActualCycle)

    @Delete
    fun delete(user: ActualCycle)
}