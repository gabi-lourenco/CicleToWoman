package com.example.cicletowoman

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.cicletowoman.utils.getDateFormatPTBR
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import kotlinx.android.synthetic.main.activity_first_period.*
import java.util.*

class FirstPeriodActivity : AppCompatActivity() {

    private var startDate: String? = null
    private var endDate: String? = null

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
                    startDate = getDateFormatPTBR(rangeManager.days.first?.calendar?.timeInMillis)
                    endDate = getDateFormatPTBR(rangeManager.days.second?.calendar?.timeInMillis)
                    Toast.makeText(
                        this@FirstPeriodActivity,
                        "Data inicial: $startDate \n Data final: $endDate",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@FirstPeriodActivity,
                        "Invalid Selection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
