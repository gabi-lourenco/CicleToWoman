package com.example.cicletowoman.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.applikeysolutions.cosmocalendar.model.Day
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.example.cicletowoman.MyApplication
import com.example.cicletowoman.R
import com.example.cicletowoman.bottomsheet.EditDayBottomSheetDialog
import com.example.cicletowoman.entities.ActualCycle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.util.*

class HistoryFragment : Fragment() {

    lateinit var auth : FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)
        auth = FirebaseAuth.getInstance()

        val cycle = MyApplication.database!!.cycleDao().findByRunning(auth.currentUser!!.uid)

        try {
            setConfigCalendar(view)
            setOnClickListeners(view)
            setDataCycleRunning(cycle, view)
        }
        catch (_: Exception) { }

        return view.rootView
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDataCycleRunning(cycle: ActualCycle, view:View) {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = cycle.startDateInMillis!!
        var day = Day(calendar)
        view.cosmo_calendar.selectionManager.toggleDay(day)
    }

    private fun setConfigCalendar(view:View) {
        view.cosmo_calendar.firstDayOfWeek = Calendar.MONDAY
        view.cosmo_calendar.selectionType = SelectionType.SINGLE
    }

    private fun setOnClickListeners(view:View) {
        view.btnEdit.setOnClickListener {
            if (view.cosmo_calendar.selectionManager is SingleSelectionManager) {
                val rangeManager = view.cosmo_calendar.selectionManager as SingleSelectionManager
                if (rangeManager != null) {
                    showBottomSheetDialogDayDetails()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.first_status_cycle_select_date,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showBottomSheetDialogDayDetails() {
        val modalBottomSheet = EditDayBottomSheetDialog()
        modalBottomSheet.show(requireActivity().supportFragmentManager, EditDayBottomSheetDialog.TAG)
    }
}
