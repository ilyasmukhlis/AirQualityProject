package com.example.airquality.ui.maps

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import androidx.core.os.bundleOf
import com.example.airquality.data.Patterns
import com.example.airquality.databinding.DialogMapParticlesIndicationsBinding
import com.example.airquality.domain.`object`.Measurements
import com.example.airquality.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * Dialog class for representing Map Indications in more detail
 */

class MapParticleIndicationDialog : BottomSheetDialogFragment() {

    private val binding: DialogMapParticlesIndicationsBinding by viewBinding()

    private val mapMeasurement: Measurements by lazyNone {
        requireArguments().getParcelable(MAP_MEASUREMENT)!!
    }

    private val location: String by lazyNone {
        requireArguments().getString(LOCATION)!!
    }

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
        setGasParticles(mapMeasurement)
        setOtherInfo(mapMeasurement)
        toolbarView.setNavigationOnClickListener {
            dismiss()
        }
        locationTextView.text = location
        dateTextView.text = convertDate(mapMeasurement.time, Patterns.yyyy_P_mm_P_dd_S_HH_DP_MM_DP_SS, Patterns.dd_S_MMMM_C_S_YYYY)
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
            temperatureTextView.text = "${it.temperature.toInt()}°C"
            humidityTextView.text = "${it.humidity.toInt()}%"
            deviceIdTextView.text = it.deviceId.toString()
            timeTextView.text = convertDate(
                it.time,
                Patterns.yyyy_P_mm_P_dd_S_HH_DP_MM_DP_SS,
                Patterns.HH_DP_MM_DP_SS
            )
        }
    }

    /**
     * companion object/static method to call dialog from other fragments/activities
     */

    companion object {
        private const val MAP_MEASUREMENT = "map_measurement"
        private const val LOCATION = "location"

        fun newInstance(mapMeasurement: Measurements, location: String) =
            MapParticleIndicationDialog().apply {
                arguments =
                    bundleOf(Pair(MAP_MEASUREMENT, mapMeasurement), Pair(LOCATION, location))
            }
    }
}