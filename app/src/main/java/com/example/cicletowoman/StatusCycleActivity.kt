package com.example.cicletowoman

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.cicletowoman.FirstPeriodActivity.Companion.START_DATE
import com.example.cicletowoman.FirstPeriodActivity.Companion.END_DATE
import kotlinx.android.synthetic.main.activity_status_cycle.*

class StatusCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_cycle)

        val startDate = intent.extras?.getString(START_DATE)
        val endDate = intent.extras?.getString(END_DATE)

        if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty()) {
            txtCycleTitle.text = getString(R.string.first_status_cycle_not_created_title)
            txtCycleMessageDescription.text = getString(R.string.first_status_cycle_not_created)
            btnCreate.isVisible = true

            btnCreate.setOnClickListener {
                Toast.makeText(
                    this,
                    "Data inicial: $startDate \n Data final: $endDate",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            txtCycleTitle.text = getString(R.string.first_status_cycle_title)
            txtCycleMessageDescription.isVisible = false
            btnCreate.isVisible = false
        }
    }
}