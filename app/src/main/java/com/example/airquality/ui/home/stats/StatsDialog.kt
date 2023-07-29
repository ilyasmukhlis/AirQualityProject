package com.example.airquality.ui.home.stats

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airquality.R
import com.example.airquality.databinding.DialogStatisticsBinding
import com.example.airquality.domain.`object`.Measurements
import com.example.airquality.domain.`object`.Pollutant
import com.example.airquality.domain.`object`.PollutantEnum
import com.example.airquality.ui.home.stats.calendar.CalendarBottomSheetDialog
import com.example.airquality.ui.home.stats.calendar.MonthlyCalendarBottomSheetDialog
import com.example.airquality.utils.core.OnItemClickListener
import com.example.airquality.utils.observe
import com.example.airquality.utils.showFullScreen
import com.example.airquality.utils.toMonth
import com.example.airquality.utils.toTime
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * StatsDialog class responsible for showing statistics of each device
 */

class StatsDialog : BottomSheetDialogFragment(), OnItemClickListener<Pollutant> {

    companion object {
        const val DEVICE_ID = "deviceID"

        fun newInstance(deviceId: String) = StatsDialog().apply {
            arguments = bundleOf(Pair(DEVICE_ID, deviceId))
        }
    }

    private val binding: DialogStatisticsBinding by viewBinding()

    private val pollutants = listOf("AQI", "H2S", "CO", "PM2.5", "O3", "CH4", "NO2")

    private val statsViewModel: StatsViewModel by sharedViewModel()

    private val currentDate = LocalDate.now()

    private var chosenItem: Pollutant? = null

    private var chosenTimeUnit: String? = null

    private var selectedDateString: String? = null

    private val pollutantsList = ArrayList<Pollutant>()

    private val pollutantsAdapter = PollutantsAdapter(this)

    private val measurementsList = arrayListOf<BarEntry>()

    private val dataSet = arrayListOf<BarDataSet>()

    private val bar = BarDataSet(measurementsList, "title")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pollutants.forEach {
            pollutantsList.add(
                Pollutant(
                    it,
                    when (it) {
                        "AQI" -> PollutantEnum.AQI
                        "H2S" -> PollutantEnum.H2S
                        "CO" -> PollutantEnum.CO
                        "O3" -> PollutantEnum.O3
                        "PM2.5" -> PollutantEnum.PM25
                        "CH4" -> PollutantEnum.CH4
                        "NO2" -> PollutantEnum.NO2
                        else -> null
                    },
                    false
                )
            )
        }
        statsViewModel.onCreate(requireArguments())
        selectedDateString =
            statsViewModel.selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return showFullScreen(super.onCreateDialog(savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statsViewModel.onViewCreated()
        observeVm()
        initTimeFrame()

        binding.toolbarView.setNavigationOnClickListener {
            dialog?.dismiss()
        }

        with(binding) {
            pollutantsRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    this.context,
                    RecyclerView.HORIZONTAL,
                    false
                )
                adapter = pollutantsAdapter
            }
            if (pollutantsAdapter.currentList.isEmpty()) {
                pollutantsAdapter.submitList(pollutantsList)
            }

            pickDateButton.setOnClickListener {
                if (chosenTimeUnit == "hourly") CalendarBottomSheetDialog().show(
                    childFragmentManager,
                    this.javaClass.name
                )
                else MonthlyCalendarBottomSheetDialog().show(
                    childFragmentManager,
                    this.javaClass.name
                )
            }
        }
    }

    private fun observeVm() = with(statsViewModel) {
        observe(measurementsDay, viewLifecycleOwner) { m ->
            chosenItem?.let {
                setData(m, it)
            }

            initChart(chosenTimeUnit.orEmpty())

            Log.e("MDay", m.toString())
        }
        observe(measurementsMonth, viewLifecycleOwner) { m ->
            chosenItem?.let {
                setData(m, it)
            }
            initChart(chosenTimeUnit.orEmpty())
            binding.barChart.notifyDataSetChanged()
            Log.e("MMonth", m.toString())
        }
        observe(pickedDate, viewLifecycleOwner) {
            if (chosenTimeUnit == "hourly") {
                selectedDateString = it.first
                binding.pickDateButton.text =
                    it.second ?: getString(R.string.pick_another_date)
                getMeasurementsDayStats(selectedDateString)
            } else {
                val date =
                    LocalDate.parse(it.first, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val days = date.until(currentDate, ChronoUnit.DAYS)
                selectedDateString = it.first
                binding.pickDateButton.text =
                    it.second ?: getString(R.string.pick_another_date)
                getMeasurementsLastDays(days.toInt())
            }
        }
        observe(setTitle, viewLifecycleOwner) {
            binding.toolbarIdTextView.text = it
        }
    }

    private fun initTimeFrame() = with(binding) {
        toggleGroup.buttons.forEach {
            it.applyToCards {
                it.layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
        }
        toggleGroup.setOnSelectListener {
            when (it.id) {
                R.id.hourlyButton -> chosenTimeUnit = "hourly"
                R.id.dailyButton -> chosenTimeUnit = "daily"
            }
        }
    }

    private fun setData(data: List<Measurements>, pollutant: Pollutant) {
        dataSet.clear()
        bar.clear()
        bar.setDrawValues(false)
        bar.barBorderWidth = 2f
        if (data.isNotEmpty()) {
            data.forEach {
                measurementsList.add(
                    BarEntry(
                        if (chosenTimeUnit == "hourly") {
                            toTime(it.time)
                        } else {
                            toMonth(it.time)
                        },
                        when (pollutant.enum) {
                            PollutantEnum.H2S -> it.h2s.toFloat()
                            PollutantEnum.CH4 -> it.ch4.toFloat()
                            PollutantEnum.CO -> it.co.toFloat()
                            PollutantEnum.NO2 -> it.nox.toFloat()
                            PollutantEnum.PM25 -> it.pm25.toFloat()
                            PollutantEnum.O3 -> it.o3.toFloat()
                            PollutantEnum.AQI -> it.quality.toFloat()
                            else -> 0.0f
                        }
                    )
                )
            }
            val barDataSet = BarDataSet(measurementsList, pollutant.title)
            dataSet.add(barDataSet)
            barDataSet.color = ContextCompat.getColor(requireContext(), R.color.colorFillOrange)
            barDataSet.valueTextColor =
                ContextCompat.getColor(requireContext(), R.color.colorGray)
            val barData = BarData(dataSet as List<IBarDataSet>?)
            binding.barChart.data = barData
        } else {
            binding.pickDateButton.isVisible = true
            binding.barChart.invalidate()
        }

    }

    private fun initChart(timeUnit: String) {
        with(binding.barChart) {
            if (timeUnit == "hourly") {
                xAxis.axisMinimum = 1f
                xAxis.axisMaximum = 24f
                description.text = "hourly"
            } else {
                xAxis.axisMinimum = 1f
                xAxis.axisMaximum = 30f
                description.text = "daily"
            }
            xAxis.labelRotationAngle = 0f
            axisRight.isEnabled = false
            xAxis.textColor = ContextCompat.getColor(context, R.color.blackStable)
            axisLeft.textColor = ContextCompat.getColor(context, R.color.blackStable)
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            setTouchEnabled(true)
            setPinchZoom(true)
            setBackgroundColor(Color.TRANSPARENT)
            animateX(1800, Easing.EaseInExpo)
        }
    }


    override fun onItemClick(item: Pollutant, position: Int) {
        chosenItem = item
        item.let {
            val newList = pollutantsAdapter.currentList.map {
                it.copy(isSelected = item.title == it.title)
            }
            pollutantsAdapter.submitList(newList)
            try {
                when (chosenTimeUnit) {
                    "hourly" -> statsViewModel.getMeasurementsDayStats(selectedDateString)
                    "daily" -> statsViewModel.getMeasurementsLastDays(30)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        pollutantsAdapter.notifyItemChanged(position)
    }
}
