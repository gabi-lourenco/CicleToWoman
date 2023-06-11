package com.example.cicletowoman.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cicletowoman.R
import com.example.cicletowoman.entities.ActualCycle
import com.example.cicletowoman.viewmodels.StatusCycleViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_status_cycle.view.*
import java.text.SimpleDateFormat
import java.util.*

class StatusCycleFragment : Fragment() {

    var startDay: String = ""
    var endDay: String = ""

    private val vm: StatusCycleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_status_cycle, container, false)

        vm.getUserLogged()

        vm.actualCycle?.observe(requireActivity()) { cycle ->
            cycle?.let {
                setDataCycleRunning(cycle, view)
            } ?: run {
                view.apply {
                    txtCycleTitle.text = getString(R.string.first_status_cycle_not_created_title)
                    txtCycleTitle.isVisible = true
                    btnCreate.isVisible = true
                    btnHistory.isVisible = false
                    btnDelete.isVisible = false
                    pieChart_view.isVisible = false

                    btnCreate.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_statusCycleFragment_to_firstPeriodFragment
                        )
                    }
                }
            }
        }

        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.statustoolbar.inflateMenu(R.menu.menu_status)
        view.statustoolbar.setTitle(R.string.first_status_cycle_title)
        view.statustoolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_edit -> {
                    findNavController().navigate(
                        R.id.action_statusCycleFragment_to_editProfileFragment
                    )
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }
    }

    private fun setDataCycleRunning(cycle: ActualCycle, view: View) {
        view.apply {
            txtCycleTitle.isVisible = false
            txtCycleMessageDescription.isVisible = false
            btnCreate.isVisible = false
            btnHistory.isVisible = true
            btnDelete.isVisible = true
        }

        startDay = getDateFormatPTBR(cycle.startDateInMillis)
        endDay = getDateFormatPTBR(cycle.endDateInMillis)

        showPieChart(cycle, view)
        view.pieChart_view.isVisible = true

        view.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_statusCycleFragment_to_historyFragment)
        }

        view.btnDelete.setOnClickListener {
            vm.actualCycle?.let {
                val builder = AlertDialog.Builder(requireActivity())

                builder.apply {
                    setTitle(R.string.first_status_cycle_delete_title)
                    setMessage(R.string.first_status_cycle_delete_messsage)
                    setPositiveButton(R.string.first_status_cycle_delete_text_yes) { _, _ ->
                        vm.deleteUserCycle()
                        if (vm.cycleDeleted == true) {
                            Toast.makeText(
                                requireActivity(),
                                R.string.first_status_cycle_delete_success,
                                Toast.LENGTH_LONG
                            ).show()

                            requireActivity().finish()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                R.string.first_status_cycle_delete_error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    setNegativeButton(R.string.first_status_cycle_delete_text_no) { _, _ -> }
                    show()
                }
            }
        }
    }

    private fun showPieChart(actualCycle: ActualCycle, view: View) {
        val pieColors: ArrayList<Int> = ArrayList()
        pieColors.add(Color.parseColor("#FF0000"))
        pieColors.add(Color.parseColor("#2473C8"))
        pieColors.add(Color.parseColor("#9919AF"))
        pieColors.add(Color.parseColor("#185EAA"))

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = actualCycle.startDateInMillis!!
        var inicialDay = endDay.substring(0, 2).toInt() - startDay.substring(0, 2).toInt()
        calendar.add(Calendar.DATE, inicialDay + 7)
        var normalOne = getDateFormatPTBR(calendar.timeInMillis)
        calendar.add(Calendar.DATE, 5)
        var fertileDays = getDateFormatPTBR(calendar.timeInMillis)
        calendar.add(Calendar.DATE, 12)
        var endDays = getDateFormatPTBR(calendar.timeInMillis)

        //initialize a List of PieEntry with its value/label pair
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        pieEntries.add(PieEntry(16f, "${startDay.substring(0, 5)} a ${endDay.substring(0, 5)}"))
        pieEntries.add(PieEntry(29f, "${endDay.substring(0, 5)} a ${normalOne.subSequence(0, 5)}"))
        pieEntries.add(PieEntry(18f, "${normalOne.subSequence(0, 5)} a ${fertileDays.subSequence(0, 5)}"))
        pieEntries.add(PieEntry(37f, "${fertileDays.subSequence(0, 5)} a ${endDays.subSequence(0, 5)}"))

        //prepare the PieDataSet with the above pieEntries and pieColors
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.valueTextSize = 14f
        pieDataSet.colors = pieColors

        //draw value/label outside the pie chart
        pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.valueLinePart1OffsetPercentage = 100f
        pieDataSet.valueLinePart1Length = 0.8f
        pieDataSet.valueLinePart2Length = 0f
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTypeface = Typeface.DEFAULT_BOLD
        pieDataSet.valueLineColor = ColorTemplate.COLOR_NONE

        //prepare the PieData
        val pieData = PieData(pieDataSet)
        pieData.setValueTextColor(Color.BLACK)
        pieData.setDrawValues(false)

        //set pieChart data and any other pieChart property needed
        view.apply {
            pieChart_view.data = pieData
            pieChart_view.setExtraOffsets(35f, 35f, 35f, 35f)
            pieChart_view.setEntryLabelColor(Color.BLACK)
            pieChart_view.setEntryLabelTextSize(14f)
            pieChart_view.setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
            pieChart_view.setUsePercentValues(true)
            pieChart_view.legend.isEnabled = false
            pieChart_view.description.isEnabled = false
            pieChart_view.description.text = "Em dias"
            pieChart_view.isRotationEnabled = true
            pieChart_view.dragDecelerationFrictionCoef = 0.9f
            pieChart_view.rotationAngle = 220f
            pieChart_view.isHighlightPerTapEnabled = true
            pieChart_view.setHoleColor(Color.WHITE)

            pieChart_view.invalidate()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFormatPTBR(timeInMillis: Long?): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        return simpleDateFormat.format(timeInMillis)
    }
}