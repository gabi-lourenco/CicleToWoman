package com.example.cicletowoman.activities

import android.icu.util.DateInterval
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.applikeysolutions.cosmocalendar.model.Day
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.example.cicletowoman.MyApplication
import com.example.cicletowoman.R
import com.example.cicletowoman.bottomsheet.EditDayBottomSheetDialog
import com.example.cicletowoman.entities.ActualCycle
import kotlinx.android.synthetic.main.activity_first_period.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.cosmo_calendar
import java.util.*


class HistoryActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val cycle = MyApplication.database!!.userDao().findByRunning()

        try {
            setConfigCalendar()
            setOnClickListeners()
            setDataCycleRunning(cycle)
        }
        catch (e: Exception) { }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDataCycleRunning(cycle: ActualCycle) {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = cycle.startDateInMillis!!
        var day = Day(calendar)
        cosmo_calendar.selectionManager.toggleDay(day)
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
