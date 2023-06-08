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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_first_period.*
import java.text.SimpleDateFormat
import java.util.*


class FirstPeriodActivity : AppCompatActivity() {

    private var startDate: String? = null
    private var endDate: String? = null

    var firebaseDatabase: FirebaseDatabase? = null

    // creating a variable for our Database
    // Reference for Firebase.
    var databaseReference: DatabaseReference? = null

    private var firstPeriodData = FirstPeriodData("", "")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_period)

        firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase?.getReference(TablesNames.TABLE_PERIOD_NAME)

        setToolbar()
        setConfigCalendar()
        setOnClickListeners()
    }

    private fun setToolbar() {
        firstPeriodtoolbar.setTitle(R.string.first_period_title)
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

                    addDataToFirebase(startDate!!, endDate!!)
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

    private fun addDataToFirebase(startDate: String, endDate: String) {
        firstPeriodData.startDate = startDate
        firstPeriodData.endData = endDate

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference!!.setValue(firstPeriodData)
                Toast.makeText(
                    this@FirstPeriodActivity, "data added", Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this@FirstPeriodActivity,
                        StatusCycleActivity::class.java).apply {
                        putExtra(START_DATE, startDate)
                        putExtra(END_DATE, endDate)
                    }
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@FirstPeriodActivity, "Fail to add data $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateFormatPTBR(timeInMillis: Long?): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        return simpleDateFormat.format(timeInMillis)
    }

    companion object {
        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"
    }
}
