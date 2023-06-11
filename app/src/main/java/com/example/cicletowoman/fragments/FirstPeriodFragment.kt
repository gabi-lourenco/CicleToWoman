package com.example.cicletowoman.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.example.cicletowoman.R
import com.example.cicletowoman.entities.ActualCycle
import androidx.fragment.app.viewModels
import com.example.cicletowoman.viewmodels.FirstPeriodViewModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_first_period.view.*
import java.text.SimpleDateFormat
import java.util.*

class FirstPeriodFragment : Fragment() {

    private var startDate: String? = null
    private var startDateTimeInMillis: Long? = null
    private var endDate: String? = null
    private var endDateTimeInMillis: Long? = null

    private val vm: FirstPeriodViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_period, container, false)

        setConfigCalendar(view)
        setOnClickListeners(view)

        return view.rootView
    }

    private fun setConfigCalendar(view: View) {
        view.cosmo_calendar.firstDayOfWeek = Calendar.MONDAY
        view.cosmo_calendar.selectionType = SelectionType.RANGE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListeners(view: View) {
        view.btnContinue.setOnClickListener {
            if (view.cosmo_calendar.selectionManager is RangeSelectionManager) {
                val rangeManager = view.cosmo_calendar.selectionManager as RangeSelectionManager
                if (rangeManager != null) {
                    startDateTimeInMillis = rangeManager.days.first?.calendar?.timeInMillis
                    startDate = getDateFormatPTBR(startDateTimeInMillis)
                    endDateTimeInMillis = rangeManager.days.second?.calendar?.timeInMillis
                    endDate = getDateFormatPTBR(endDateTimeInMillis)

                    val calendar: Calendar = Calendar.getInstance()
                    calendar.timeInMillis = startDateTimeInMillis!!
                    var inicialDay = endDate!!
                        .substring(0, 2).toInt() - startDate!!.substring(0, 2).toInt()
                    calendar.add(Calendar.DATE, inicialDay + 7)
                    var normalOne = getDateFormatPTBR(calendar.timeInMillis)
                    calendar.add(Calendar.DATE, 5)
                    var fertileDays = getDateFormatPTBR(calendar.timeInMillis)
                    calendar.add(Calendar.DATE, 12)
                    var endDays = getDateFormatPTBR(calendar.timeInMillis)

                    vm.insertFirstPeriod(
                        ActualCycle(
                            uid = vm.getUserLoggedUid(),
                            startDate = startDate,
                            endDate = endDate,
                            neutralStartFirst = endDate,
                            neutralEndFirst = normalOne,
                            startFertility = normalOne,
                            endFertility = fertileDays,
                            neutralStartLast = fertileDays,
                            neutralEndLast = endDays,
                            startDateInMillis = startDateTimeInMillis,
                            endDateInMillis = endDateTimeInMillis,
                            actual = true
                        )
                    )

                    if (vm.cycleInserted == true) {
                        findNavController().navigate(
                            R.id.action_firstPeriodFragment_to_statusCycleFragment
                        )
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            R.string.first_status_cycle_insert_error,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.first_status_cycle_invalid_selection,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        view.btnLater.setOnClickListener {
            findNavController().navigate(R.id.action_firstPeriodFragment_to_statusCycleFragment)
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
