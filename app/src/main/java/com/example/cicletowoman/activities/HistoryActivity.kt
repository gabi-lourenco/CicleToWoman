package com.example.cicletowoman.activities

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.example.cicletowoman.R
import com.example.cicletowoman.bottomsheet.EditDayBottomSheetDialog
import kotlinx.android.synthetic.main.activity_first_period.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.cosmo_calendar
import java.util.*

class HistoryActivity : AppCompatActivity() {

    var startDateMillis: Long? = null
    var endDateMillis: Long? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        startDateMillis = intent.extras?.getLong(FirstPeriodActivity.START_DATE_MILLIS)
        endDateMillis = intent.extras?.getLong(FirstPeriodActivity.END_DATE_MILLIS)

        setConfigCalendar()
        setOnClickListeners()
        test()
    }

    private fun test() {
        var days = TreeSet<Long>();
        days.add(startDateMillis!!)
        days.add(endDateMillis!!)

        var textColor = Color.parseColor("#ff0000")
        var selectedTextColor = Color.parseColor("#ff4000")
        var disabledTextColor = Color.parseColor("#ff8000")
        var connectedDays = ConnectedDays(days, textColor, selectedTextColor, disabledTextColor)

        cosmo_calendar.addConnectedDays(connectedDays)
    }


    private fun setConfigCalendar() {
        cosmo_calendar.firstDayOfWeek = Calendar.MONDAY
        cosmo_calendar.selectionType = SelectionType.SINGLE
    }

    private fun setOnClickListeners() {
        btnEdit.setOnClickListener {
            if (cosmo_calendar.selectionManager is SingleSelectionManager) {
                val rangeManager = cosmo_calendar.selectionManager as SingleSelectionManager
                if (rangeManager != null) {
                    showBottomSheetDialogDayDetails()
                } else {
                    Toast.makeText(
                        this,
                        "Selecione uma data para editar.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showBottomSheetDialogDayDetails() {
        val modalBottomSheet = EditDayBottomSheetDialog()
        modalBottomSheet.show(supportFragmentManager, EditDayBottomSheetDialog.TAG)
    }
}
