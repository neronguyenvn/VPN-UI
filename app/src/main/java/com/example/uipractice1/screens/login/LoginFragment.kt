package com.example.uipractice1.screens.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.uipractice1.R
import com.example.uipractice1.databinding.FragmentLoginBinding
import java.util.concurrent.Executor


class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)


        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", null)
        val password = sharedPref?.getString("password", null)
        if (username != null && password != null)
            this.findNavController().navigate((R.id.action_loginFragment_to_profileFragment))

        val executor = ContextCompat.getMainExecutor(context)

        binding.apply {
            loginButton.setOnClickListener { view ->
                if (nameEdit.text.toString() == "abc" && passEdit.text.toString() == "123") {
                    with(sharedPref!!.edit()) {
                        putString("username", nameEdit.text.toString())
                        putString("password", passEdit.text.toString())
                        apply()
                    }

                    authUser(executor)

                    view.findNavController()
                        .navigate(R.id.action_loginFragment_to_profileFragment)
                }
            }
        }
        return binding.root
    }

    private fun authUser(executor: Executor) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setDescription("Touch here")
            .setDeviceCredentialAllowed(true)
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        context,
                        "succeeded",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        context,
                        "error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        context,
                        "failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        biometricPrompt.authenticate(promptInfo)
    }
}
