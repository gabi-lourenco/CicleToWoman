package com.example.cicletowoman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_first_period.*
import java.util.*


class FirstPeriodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_period)

        cosmo_calendar.firstDayOfWeek = Calendar.MONDAY
    }
}