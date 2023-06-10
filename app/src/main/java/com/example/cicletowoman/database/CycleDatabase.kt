package com.example.cicletowoman.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cicletowoman.dao.ActualCycleDao
import com.example.cicletowoman.dao.ProfileDao
import com.example.cicletowoman.entities.ActualCycle
import com.example.cicletowoman.entities.Profile

@Database(entities = [ActualCycle::class, Profile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): ActualCycleDao

    abstract fun profileDao(): ProfileDao
}