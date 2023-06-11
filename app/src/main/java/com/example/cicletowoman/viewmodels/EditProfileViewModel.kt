package com.example.cicletowoman.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cicletowoman.MyApplication
import com.example.cicletowoman.entities.Profile
import com.google.firebase.auth.FirebaseAuth

class EditProfileViewModel : ViewModel() {

    var auth = FirebaseAuth.getInstance()
    var profileData: MutableLiveData<Profile>? = MutableLiveData()
    var dataDeleted: Boolean? = null

    fun getUserLoggedUid(): String {
        return auth.currentUser!!.uid
    }

    fun getUserLoggedName(): String {
        return auth.currentUser!!.displayName!!
    }

    fun getUserProfile() {
        try {
            val profile = MyApplication.database!!
                .profileDao()
                .getByUid(
                    auth?.currentUser!!.uid
                )
            profileData?.value = profile
        } catch (e: Exception) { }
    }

    fun deleteUserCycle() {
        dataDeleted = try {
            MyApplication.database!!.cycleDao().delete(auth.currentUser!!.uid)
            MyApplication.database!!.profileDao().delete(auth.currentUser!!.uid)
            true
        } catch (e: Exception) {
            false
        }
    }
}