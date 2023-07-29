package com.example.airquality.ui.home.stats.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airquality.databinding.DialogCalendarBinding
import com.example.airquality.ui.di.presentationModule
import com.example.airquality.ui.home.stats.StatsViewModel
import com.example.airquality.utils.monthPrettify
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.context.loadKoinModules
import java.time.LocalDate
import java.time.YearMonth

/**
 * CalendarBottomSheetDialog class to display calendar
 */
class CalendarBottomSheetDialog : BottomSheetDialogFragment(),
    CalendarAdapter.OnCalendarItemListener {

    private val binding: DialogCalendarBinding by viewBinding()
    private val viewModel: StatsViewModel by sharedViewModel()

    private var lastSelectedMonth: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadKoinModules(presentationModule)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        setMonthView()
    }

    private fun setupButtons() {
        lastSelectedMonth = viewModel.selectedDate.month.value
        binding.previousMonthButton.setOnClickListener {
            viewModel.selectedDate = viewModel.selectedDate.minusMonths(1)
            setMonthView()
        }
        binding.nextMonthButton.setOnClickListener {
            viewModel.selectedDate = viewModel.selectedDate.plusMonths(1)
            setMonthView()
        }
    }

    /**
     * Setting Month View for hourly time scale
     */
    @SuppressLint("SetTextI18n")
    private fun setMonthView() {
        binding.monthYearTextView.text = monthYearFromDate(viewModel.selectedDate)
        val daysInMonth: ArrayList<String> = daysInMonthArray(viewModel.selectedDate)
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(context, 7)
        binding.calendarRecyclerView.adapter =
            CalendarAdapter(daysInMonth, viewModel.selectedDate, requireContext(), this, lastSelectedMonth)
    }

    /**
     * Calculating days in Month to correctly fit the calendar
     */
    private fun daysInMonthArray(date: LocalDate?): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = viewModel.selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    /**
     * Getting month name from date
     */
    private fun monthYearFromDate(date: LocalDate?): String {
        return "${date?.month.toString().monthPrettify()} ${date?.year}"
    }

    /**
     * Processing calendar item/day clicked
     */
    override fun onItemClick(position: Int, dayText: String?) {
        if (!dayText.isNullOrEmpty()) {
            lastSelectedMonth = viewModel.selectedDate.month.value
            viewModel.selectedDate = viewModel.selectedDate.withDayOfMonth(dayText.toInt())
        }
        viewModel.selectedDate.let {
            viewModel.onDatePicked(
                "${it.year}-${it.month.value}-$dayText",
                dayText + " " + monthYearFromDate(viewModel.selectedDate)
            )
            dismiss()
        }

    }
}