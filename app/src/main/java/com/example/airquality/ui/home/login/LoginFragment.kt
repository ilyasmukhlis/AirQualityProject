package com.example.airquality.ui.home.login

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.airquality.R
import com.example.airquality.databinding.FragmentLoginBinding
import com.example.airquality.ui.home.main.HomeViewModel
import com.example.airquality.utils.EventObserver
import com.example.airquality.utils.observe
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * LoginFragment Class, responsible login process
 */

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val homeViewModel: HomeViewModel by sharedViewModel()
    private val binding: FragmentLoginBinding by viewBinding()

    private var email: String = ""
    private var passwd: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView() = with(binding) {
        username.addTextChangedListener {
            email = it.toString()
        }

        password.addTextChangedListener {
            passwd = it.toString()
        }

        login.setOnClickListener {
            homeViewModel.login(email = email, password = passwd)
        }

        register.setOnClickListener {
            findNavController().navigate(R.id.login_to_register)
        }
    }

    private fun observeViewModel() = with(homeViewModel) {
        observe(closeLogin, viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        showToast.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(binding.root.context.applicationContext, it, Toast.LENGTH_LONG).show()
        })
    }
}