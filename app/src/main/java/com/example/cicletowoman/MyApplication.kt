package com.example.cicletowoman

import android.app.Application
import androidx.room.Room
import com.example.cicletowoman.database.AppDatabase
import com.google.firebase.auth.FirebaseAuth

open class MyApplication : Application() {

    companion object {
        var database: AppDatabase? = null
        var auth : FirebaseAuth? = null
    }

    override fun onCreate() {
        super.onCreate()
        //Room
        database = Room.databaseBuilder(
            this, AppDatabase::class.java, "cycledb"
        ).allowMainThreadQueries().build()

        // Auth
        auth = FirebaseAuth.getInstance()
    }
}