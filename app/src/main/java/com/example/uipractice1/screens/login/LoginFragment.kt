package com.example.uipractice1.screens.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.uipractice1.R
import com.example.uipractice1.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModel
        val executor = ContextCompat.getMainExecutor(context)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)


        binding.apply {
            loginButton.setOnClickListener {
                viewModel.loginButtonClick(nameEdit, passEdit, requireContext())
            }
        }

        viewModel.haveLoggedIn(requireActivity(), executor, this, sharedPref)

        viewModel.checkLogin.observe(viewLifecycleOwner, Observer { check ->
            when (check) {
                1 -> {
                    viewModel.createAlertDialog(requireActivity(), executor, sharedPref, this)
                    viewModel.createSharedPref(
                        requireActivity(),
                        binding.nameEdit, binding.passEdit,
                        sharedPref
                    )
                }
            }
        })

        viewModel.checkNav.observe(viewLifecycleOwner, Observer { check ->
            when (check) {
                1 -> {
                    this.findNavController().navigate(R.id.action_login_to_profile)
                    viewModel.doneNav()
                }
            }
        })
        return binding.root
    }
}
