package com.example.cicletowoman.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.example.cicletowoman.R
import com.example.cicletowoman.constants.TablesNames
import com.example.cicletowoman.data.FirstPeriodData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_first_period.*
import java.text.SimpleDateFormat
import java.util.*


class FirstPeriodActivity : AppCompatActivity() {

    private var startDate: String? = null
    private var startDateTimeInMillis: Long? = null
    private var endDate: String? = null
    private var endDateTimeInMillis: Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_period)


        setConfigCalendar()
        setOnClickListeners()
    }

    private fun setConfigCalendar() {
        cosmo_calendar.firstDayOfWeek = Calendar.MONDAY
        cosmo_calendar.selectionType = SelectionType.RANGE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListeners() {
        btnContinue.setOnClickListener {
            if (cosmo_calendar.selectionManager is RangeSelectionManager) {
                val rangeManager = cosmo_calendar.selectionManager as RangeSelectionManager
                if (rangeManager != null) {
                    startDateTimeInMillis = rangeManager.days.first?.calendar?.timeInMillis
                    startDate = getDateFormatPTBR(startDateTimeInMillis)
                    endDateTimeInMillis = rangeManager.days.second?.calendar?.timeInMillis
                    endDate = getDateFormatPTBR(endDateTimeInMillis)

                    startActivity(
                        Intent(
                            this@FirstPeriodActivity,
                            StatusCycleActivity::class.java).apply {
                            putExtra(START_DATE, startDate)
                            putExtra(END_DATE, endDate)
                            putExtra(START_DATE_MILLIS, rangeManager.days.first?.calendar?.timeInMillis)
                            putExtra(END_DATE_MILLIS, rangeManager.days.second?.calendar?.timeInMillis)
                        }
                    )
                } else {
                    Toast.makeText(
                        this@FirstPeriodActivity,
                        "Invalid Selection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnLater.setOnClickListener {
            startActivity(
                Intent(this@FirstPeriodActivity, StatusCycleActivity::class.java)
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFormatPTBR(timeInMillis: Long?): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        return simpleDateFormat.format(timeInMillis)
    }

    companion object {
        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"
        const val START_DATE_MILLIS = "START_DATE_MILLIS"
        const val END_DATE_MILLIS = "END_DATE_MILLIS"
    }
}
