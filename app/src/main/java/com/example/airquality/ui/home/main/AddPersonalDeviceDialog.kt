package com.example.airquality.ui.home.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import androidx.core.widget.addTextChangedListener
import com.example.airquality.R
import com.example.airquality.databinding.AddPersonalDeviceDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Dialog for adding personal device
 */

class AddPersonalDeviceDialog : BottomSheetDialogFragment() {

    private val binding: AddPersonalDeviceDialogBinding by viewBinding()
    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun getTheme() = R.style.SheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deviceIdEditText.addTextChangedListener {
            homeViewModel.deviceId = it.toString().toInt()
        }

        binding.addDeviceButton.setOnClickListener {
            homeViewModel.addDeviceToUser()
            dismiss()
        }
    }
}