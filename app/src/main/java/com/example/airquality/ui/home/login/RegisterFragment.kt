package com.example.airquality.ui.home.login

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.airquality.R
import com.example.airquality.databinding.FragmentRegisterBinding
import com.example.airquality.ui.home.main.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * RegisterFragment Class, responsible registration process
 */

class RegisterFragment: Fragment(R.layout.fragment_register) {

    private val homeViewModel: HomeViewModel by sharedViewModel()

    private val binding: FragmentRegisterBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() = with(binding) {
        nameEditText.addTextChangedListener {
            homeViewModel.name = it.toString()
        }

        surnameEditText.addTextChangedListener {
            homeViewModel.surname = it.toString()
        }

        emailEditText.addTextChangedListener {
            homeViewModel.email = it.toString()
        }

        phoneEditText.addTextChangedListener {
            homeViewModel.phone = it.toString()
        }

        passwordEditText.addTextChangedListener {
            homeViewModel.passwd = it.toString()
        }

        organisationEditText.addTextChangedListener {
            homeViewModel.organization = it.toString()
        }

        toolbarView.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        register.setOnClickListener {
            homeViewModel.register()
            findNavController().navigateUp()
        }

    }
}