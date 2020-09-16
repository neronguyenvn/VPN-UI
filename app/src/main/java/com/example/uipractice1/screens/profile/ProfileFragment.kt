package com.example.uipractice1.screens.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.uipractice1.R
import com.example.uipractice1.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding.lifecycleOwner = this

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_3 -> {

                    viewModel.page3Click(sharedPref, requireActivity())
                    true
                }
                else -> false
            }
        }

        viewModel.checkNav.observe(viewLifecycleOwner, { check ->
            when (check) {
                1 -> {

                    this.findNavController().navigate(R.id.action_profile_to_login)
                    viewModel.doneNav()
                }
            }
        })
        return binding.root
    }
}
