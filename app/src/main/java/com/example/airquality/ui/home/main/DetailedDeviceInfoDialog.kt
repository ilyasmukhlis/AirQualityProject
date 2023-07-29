package com.example.airquality.ui.home.main

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import androidx.core.os.bundleOf
import com.example.airquality.R
import com.example.airquality.data.Patterns
import com.example.airquality.databinding.DialogDetailedDeviceInfoBinding
import com.example.airquality.domain.`object`.Measurements
import com.example.airquality.ui.home.stats.StatsDialog
import com.example.airquality.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Dialog for representing more info about the device
 */

class DetailedDeviceInfoDialog : BottomSheetDialogFragment() {

    private val binding: DialogDetailedDeviceInfoBinding by viewBinding()
    private val homeViewModel: HomeViewModel by sharedViewModel()

    private val measurement: Measurements by lazyNone {
        requireArguments().getParcelable(MEASUREMENT)!!
    }

    private val location: String by lazyNone {
        requireArguments().getString(LOCATION)!!
    }

    private var lastDeviceAqi: Pair<Int, Int> = Pair(0, 0)

    var onDismiss = {}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    /**
     * it is required to override onCreateDialog to show full screen dialog
     */

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return showFullScreen(super.onCreateDialog(savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(measurement)
        observeViewModel()
    }

    private fun observeViewModel() = with(homeViewModel) {
        observe(measurements, viewLifecycleOwner) {
            setupView(it.lastOrNull())
        }
    }

    /**
     * Method responsible for setting views in place
     */

    private fun setupView(measurement: Measurements?) = with(binding) {
        setGasParticles(measurement)
        setOtherInfo(measurement)

        measurement?.let {
            if (lastDeviceAqi.first != measurement.deviceId && lastDeviceAqi.second != measurement.quality.toInt()) {
                lastDeviceAqi = Pair(measurement.deviceId, measurement.quality.toInt())
                val sections = aqiCircleColorify(measurement.quality.toInt(), requireContext())
                aqiDonutProgress.cap = 500f
                aqiDonutProgress.clear()
                aqiDonutProgress.submitData(sections)
            }

            aqiScoreTextView.text = measurement.quality.toInt().toString()
            aqiStatusTextView.text = context?.getString(statusChecker(measurement.quality.toInt()))

            toolbarView.setNavigationOnClickListener {
                dismiss()
            }

            statisticsButton.setOnClickListener {
                StatsDialog
                    .newInstance(deviceId = measurement.deviceId.toString())
                    .show(childFragmentManager, this.javaClass.name)
            }

            locationTextView.text = location
            dateTextView.text = convertDate(
                measurement.time,
                Patterns.yyyy_P_mm_P_dd_S_HH_DP_MM_DP_SS,
                Patterns.dd_S_MMMM_C_S_YYYY
            )

            homeViewModel.getMeasurementByDeviceId()
        }
    }

    /**
     * Method responsible for setting gas indication status
     */

    private fun setGasParticles(measurement: Measurements?) = with(binding.particleIndications) {
        measurement?.let { measurement ->
            setGasParticlesProgressBar(measurement)
            pm25IndicationTextView.text = measurement.pm25.toString()
            ch3IndicationTextView.text = measurement.ch4.toString()
            o3IndicationTextView.text = measurement.o3.toString()
            no2IndicationTextView.text = measurement.nox.toString()
            h2sIndicationTextView.text = measurement.h2s.toString()
            coIndicationTextView.text = measurement.co.toString()
        }
    }

    /**
     * Method responsible for setting gas indication line
     */

    private fun setGasParticlesProgressBar(measurement: Measurements?) =
        with(binding.particleIndications) {
            measurement?.let {
                pm25ProgressBar.apply {
                    progressTintList =
                        ColorStateList.valueOf(identifyColorByAqi(it.pm25.toInt(), context))
                    progress = it.pm25.toInt()
                }
                ch3ProgressBar.apply {
                    progressTintList =
                        ColorStateList.valueOf(identifyColorByAqi(it.ch4.toInt(), context))
                    progress = it.ch4.toInt()
                }
                o3ProgressBar.apply {
                    progressTintList =
                        ColorStateList.valueOf(identifyColorByAqi(it.o3.toInt(), context))
                    progress = it.o3.toInt()
                }
                no2ProgressBar.apply {
                    progressTintList =
                        ColorStateList.valueOf(identifyColorByAqi(it.nox.toInt(), context))
                    progress = it.nox.toInt()
                }
                h2sProgressBar.apply {
                    progressTintList =
                        ColorStateList.valueOf(identifyColorByAqi(it.h2s.toInt(), context))
                    progress = it.h2s.toInt()
                }
                coProgressBar.apply {
                    progressTintList =
                        ColorStateList.valueOf(identifyColorByAqi(it.co.toInt(), context))
                    progress = it.co.toInt()
                }
            }
        }

    /**
     * Method responsible for setting other info fields
     */

    private fun setOtherInfo(measurement: Measurements?) = with(binding.otherIndications) {
        measurement?.let {

            temperatureTextView.text = root.context.getString(R.string.temperature_s, it.temperature.toInt().toString())
            humidityTextView.text = "${it.humidity.toInt()}%"
            deviceIdTextView.text = it.deviceId.toString()
            timeTextView.text = convertDate(
                it.time,
                Patterns.yyyy_P_mm_P_dd_S_HH_DP_MM_DP_SS,
                Patterns.HH_DP_MM_DP_SS
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke()
    }


    /**
     * companion object/static method to call dialog from other fragments/activities
     */

    companion object {
        private const val MEASUREMENT = "measurement"
        private const val LOCATION = "location"

        fun newInstance(measurement: Measurements, location: String) =
            DetailedDeviceInfoDialog().apply {
                arguments =
                    bundleOf(Pair(MEASUREMENT, measurement), Pair(LOCATION, location))
            }
    }

}