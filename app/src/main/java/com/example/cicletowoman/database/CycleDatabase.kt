package com.example.cicletowoman.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cicletowoman.dao.ActualCycleDao
import com.example.cicletowoman.entities.ActualCycle

@Database(entities = [ActualCycle::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): ActualCycleDao
}