package com.example.cicletowoman

import android.app.Application
import androidx.room.Room
import com.example.cicletowoman.database.AppDatabase

open class MyApplication : Application() {

    companion object {
        var database: AppDatabase? = null
    }


    override fun onCreate() {
        super.onCreate()
        //Room
        database = Room.databaseBuilder(
            this, AppDatabase::class.java, "cycledb"
        ).allowMainThreadQueries().build()
    }
}