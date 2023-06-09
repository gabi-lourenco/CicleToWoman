package com.example.cicletowoman.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.cicletowoman.R
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.END_DATE
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.END_DATE_MILLIS
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.START_DATE
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.START_DATE_MILLIS
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_status_cycle.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatusCycleActivity : AppCompatActivity() {

    var startDay: String = ""
    var endDay: String = ""
    var startDateMillis: Long? = null
    var endDateMillis: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_cycle)

        val startDate = intent.extras?.getString(START_DATE)
        startDateMillis = intent.extras?.getLong(START_DATE_MILLIS)
        val endDate = intent.extras?.getString(END_DATE)
        endDateMillis = intent.extras?.getLong(END_DATE_MILLIS)

        if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty()) {
            txtCycleTitle.text = getString(R.string.first_status_cycle_not_created_title)
            txtCycleMessageDescription.text = getString(R.string.first_status_cycle_not_created)
            btnCreate.isVisible = true
            btnHistory.isVisible = false
            pieChart_view.isVisible = false

            btnCreate.setOnClickListener {
                startActivity(
                    Intent(
                        this@StatusCycleActivity, FirstPeriodActivity::class.java)
                )
            }
        } else {
            txtCycleTitle.text = getString(R.string.first_status_cycle_title)
            txtCycleMessageDescription.isVisible = false
            btnCreate.isVisible = false
            btnHistory.isVisible = true

            startDay = getDateFormatPTBR(startDateMillis)
            endDay = getDateFormatPTBR(endDateMillis)

            showPieChart()

            pieChart_view.isVisible = true

            btnHistory.setOnClickListener {
                startActivity(
                    Intent(
                        this@StatusCycleActivity,
                        HistoryActivity::class.java).apply {
                            putExtra(START_DATE, startDateMillis)
                            putExtra(END_DATE, endDateMillis)
                    }
                )
            }
        }
    }

    private fun showPieChart() {
        val pieColors: ArrayList<Int> = ArrayList()
        pieColors.add(Color.parseColor("#FF0000"))
        pieColors.add(Color.parseColor("#2473C8"))
        pieColors.add(Color.parseColor("#9919AF"))
        pieColors.add(Color.parseColor("#185EAA"))

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = startDateMillis!!
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

    @SuppressLint("SimpleDateFormat")
    private fun getDateFormatPTBR(timeInMillis: Long?): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        return simpleDateFormat.format(timeInMillis)
    }
}