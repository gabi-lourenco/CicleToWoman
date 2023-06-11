package com.example.cicletowoman.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cicletowoman.MyApplication
import com.example.cicletowoman.entities.ActualCycle

class StatusCycleViewModel : ViewModel() {

    var actualCycle: MutableLiveData<ActualCycle>? = MutableLiveData()
    var cycleDeleted: Boolean? = null

    fun getUserLogged() {
        try {
            val cycle = MyApplication.database!!
                .cycleDao()
                .findByRunning(
                    MyApplication.auth?.currentUser!!.uid
                )
            actualCycle?.value = cycle
        } catch (e: Exception) { }
    }

    fun deleteUserCycle() {
        cycleDeleted = try {
            MyApplication.database!!
                .cycleDao()
                .delete(
                    actualCycle?.value!!.uid
                )
            true
        } catch (e: Exception) {
            false
        }
    }
}