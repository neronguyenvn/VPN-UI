package com.example.uipractice1.screens.profile

import android.app.Activity
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uipractice1.screens.login.LoginViewModel

class ProfileViewModel : ViewModel() {
    private val loginViewModel = LoginViewModel()

    fun page3Click(
        sharedPref: SharedPreferences,
        activity: Activity
    ) {
        logOut(sharedPref, activity)
    }


    private fun logOut(sharedPref: SharedPreferences, activity: Activity) {
        with(sharedPref.edit()) {
            putString(loginViewModel.getSharedPrefUsername(activity), "")
            putString(loginViewModel.getSharedPrefPassword(activity), "")
            apply()
            doNav()
        }
    }

    private val _checkNav = MutableLiveData<Int>()
    val checkNav: LiveData<Int>
        get() = _checkNav

    private fun doNav() {
        _checkNav.value = 1
    }

    fun doneNav() {
        _checkNav.value = -1
    }
}