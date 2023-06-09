package com.example.cicletowoman.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.cicletowoman.R
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.END_DATE
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.END_DATE_MILLIS
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.START_DATE
import com.example.cicletowoman.activities.FirstPeriodActivity.Companion.START_DATE_MILLIS
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.activity_status_cycle.*

class StatusCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_cycle)

        val startDate = intent.extras?.getString(START_DATE)
        val startDateMillis = intent.extras?.getLong(START_DATE_MILLIS)
        val endDate = intent.extras?.getString(END_DATE)
        val endDateMillis = intent.extras?.getLong(END_DATE_MILLIS)

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
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "type"

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["Período"] = 200
        typeAmountMap["Fertilidade"] = 600
        typeAmountMap["Possível fertilidade"] = 100
        typeAmountMap["Ovulação"] = 100

        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        pieDataSet.setColors(
            resources.getColor(R.color.colorRed),
            resources.getColor(R.color.colorBlue),
            resources.getColor(R.color.colorGray),
            resources.getColor(R.color.colorBlue)
        )
        //setting text size of the value
        pieDataSet.valueTextSize = 16f

        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        pieChart_view.description.isEnabled = false
        pieChart_view.data = pieData
        pieChart_view.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        pieChart_view.holeRadius = 70f
        pieChart_view.transparentCircleRadius = 0f
        pieChart_view.setDrawSliceText(false)
        pieChart_view.invalidate()
    }
}