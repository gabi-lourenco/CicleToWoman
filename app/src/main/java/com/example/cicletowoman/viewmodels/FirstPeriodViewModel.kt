package com.example.cicletowoman.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cicletowoman.MyApplication
import com.example.cicletowoman.entities.ActualCycle
import com.google.firebase.auth.FirebaseAuth

class FirstPeriodViewModel : ViewModel() {

    var auth = FirebaseAuth.getInstance()
    var actualCycle: MutableLiveData<ActualCycle>? = MutableLiveData()
    var cycleInserted: Boolean? = null

    fun getUserLoggedUid(): String {
        return auth.currentUser!!.uid
    }

    fun insertFirstPeriod(actualCycle : ActualCycle) {
        cycleInserted = try {
            MyApplication.database!!
                .cycleDao()
                .insert(actualCycle)
            true
        } catch (e: Exception) {
            false
        }
    }
}