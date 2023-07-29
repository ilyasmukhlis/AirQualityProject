package com.example.airquality.ui.home.settings

import android.os.Bundle
import android.view.View
import android.view.Window
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.airquality.R
import com.example.airquality.databinding.FragmentSettingsBinding
import com.example.airquality.ui.home.main.HomeViewModel
import com.example.airquality.utils.*
import com.example.airquality.utils.theme.Theme
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*



/**
 * SettingsFragment class for showing general available settings
 */

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()
    private val homeViewModel: HomeViewModel by sharedViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeViewModel()
    }

    private fun setupView() = with(binding) {
        changePassword.setOnClickListener {
            val dialog = ChangePasswordDialog()
            dialog.dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.show(childFragmentManager, this.javaClass.name)
        }
        toolbarView.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        activity?.let {
            when (it.getUITheme()) {
                Theme.APP -> {
                    modeToggleGroup.selectButton(R.id.dayButton)
                }
                Theme.DARK -> {
                    modeToggleGroup.selectButton(R.id.nightButton)
                }
                Theme.SYSTEM -> {
                    modeToggleGroup.selectButton(R.id.systemButton)
                }
            }

            when (root.context.getLanguage()) {
                "kk" -> langToggleGroup.selectButton(R.id.kazButton)
                "ru" -> langToggleGroup.selectButton(R.id.rusButton)
                else -> langToggleGroup.selectButton(R.id.engButton)
            }
        }

        modeToggleGroup.setOnSelectListener { button ->
            activity?.let {
                when (button.id) {
                    R.id.dayButton -> it.setTheme(Theme.APP)
                    R.id.nightButton -> it.setTheme(Theme.DARK)
                    R.id.systemButton -> it.setTheme(Theme.SYSTEM)
                }
                it.recreate()
            }

        }

        langToggleGroup.setOnSelectListener { button ->
            activity?.let {
                when (button.id) {
                    R.id.kazButton -> {
                        it.changeLocale(Locale("kk"))
                    }
                    R.id.rusButton -> {
                        it.changeLocale(Locale("ru"))
                    }
                    R.id.engButton -> {
                        it.changeLocale(Locale("en"))
                    }
                }

            }
        }

    }

    private fun observeViewModel() = with(homeViewModel) {
        isLogged.observe(viewLifecycleOwner) {
//            binding.personalSettings.isVisible = it
            //TODO Fix when Backend will be ready
        }
    }
}