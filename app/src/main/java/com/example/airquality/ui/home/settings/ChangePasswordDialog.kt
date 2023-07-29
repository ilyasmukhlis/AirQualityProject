package com.example.airquality.ui.home.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import androidx.core.widget.addTextChangedListener
import com.example.airquality.databinding.ChangePasswdDialogBinding
import com.example.airquality.ui.home.main.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * Dialog for changing user password
 */

class ChangePasswordDialog : BottomSheetDialogFragment() {

    private val binding: ChangePasswdDialogBinding by viewBinding()

    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() = with(binding) {
        username.setText(homeViewModel.email)
        username.isEnabled = false

        password.addTextChangedListener {
            homeViewModel.passwd = it.toString()
        }

        saveButton.setOnClickListener {
            homeViewModel.changeCreds()
            dismiss()
        }
    }


}