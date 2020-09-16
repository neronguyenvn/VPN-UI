package com.example.uipractice1.screens.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uipractice1.R
import java.util.concurrent.Executor

class LoginViewModel : ViewModel() {
    private val _checkLogin = MutableLiveData<Int>()
    val checkLogin: LiveData<Int>
        get() = _checkLogin


    fun loginButtonClick(nameEdit: EditText, passEdit: EditText, context: Context) {
        if (nameEdit.text.toString() == "a" && passEdit.text.toString() == "1")
            loginSuccess()
        else loginFailed(context)
    }

    fun haveLoggedIn(
        activity: Activity,
        executor: Executor,
        fragment: Fragment,
        sharedPref: SharedPreferences
    ) {
        if (sharedPref.getString(
                getSharedPrefUsername(activity), ""
            ) == "a"
            && sharedPref.getString(
                getSharedPrefPassword(activity), ""
            ) == "1"
        ) if (haveActiveFA(activity, sharedPref))
            authUser(executor, fragment, activity, sharedPref)
        else doNav()
    }

    fun haveActiveFA(activity: Activity, sharedPref: SharedPreferences): Boolean {
        return sharedPref.getInt(
            getSharedPrefActiveFA(activity), -1
        ) == 1
    }

    fun getSharedPrefUsername(activity: Activity) =
        activity.getString(R.string.sharedPref_username)

    fun getSharedPrefPassword(activity: Activity) =
        activity.getString(R.string.sharedPref_password)

    fun getSharedPrefActiveFA(activity: Activity) = activity.getString(R.string.sharedPref_activeFA)

    private val _checkNav = MutableLiveData<Int>()
    val checkNav: LiveData<Int>
        get() = _checkNav

    fun doNav() {
        _checkNav.value = 1
    }

    private fun loginSuccess() {
        _checkLogin.value = 1
    }

    fun createSharedPref(
        activity: Activity,
        nameEdit: EditText,
        passEdit: EditText,
        sharedPref: SharedPreferences
    ) {
        with(sharedPref.edit()) {
            putString(getSharedPrefUsername(activity), nameEdit.text.toString())
            putString(getSharedPrefPassword(activity), passEdit.text.toString())
            apply()
        }
    }

    private fun loginFailed(context: Context) {
        Toast.makeText(context, R.string.error_msg_login_failed, Toast.LENGTH_SHORT).show()
    }

    fun createAlertDialog(
        activity: Activity,
        executor: Executor,
        sharedPref: SharedPreferences,
        fragment: Fragment
    ) {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setMessage("Add fingersprint authentication for later login?")
            .setPositiveButton("Agree") { _, _ ->
                authUser(executor, fragment, activity, sharedPref)
            }
            .setNegativeButton("Cancel") { _, _ ->
                doNav()
            }
        alertDialog.show()
    }

    private fun authUser(
        executor: Executor,
        fragment: Fragment,
        activity: Activity,
        sharedPref: SharedPreferences
    ) {
        val biometricPrompt = BiometricPrompt(fragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    if (haveActiveFA(activity, sharedPref))
                        doNav()
                    else {
                        with(sharedPref.edit()) {
                            putInt(getSharedPrefActiveFA(activity), 1)
                            apply()
                        }
                        doNav()
                    }
                }
            })
        biometricPrompt.authenticate(promptInfo)
    }


    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Fingersprint Authentication")
        .setSubtitle("Touch here to authenticate your fingersprint")
        .setDeviceCredentialAllowed(true)
        .build()

    fun doneNav() {
        _checkNav.value = 0
    }
}