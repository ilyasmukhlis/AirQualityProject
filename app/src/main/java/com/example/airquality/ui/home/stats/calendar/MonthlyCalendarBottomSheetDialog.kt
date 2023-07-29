package com.example.airquality.ui.home.stats.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.bottomsheetdialogfragment.viewBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.example.airquality.databinding.DialogMonthCalendarBinding
import com.example.airquality.ui.di.presentationModule
import com.example.airquality.ui.home.stats.StatsViewModel
import com.example.airquality.utils.monthPrettify
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.context.loadKoinModules
import java.text.DateFormatSymbols
import java.time.LocalDate


/**
 * MonthlyCalendarBottomSheetDialog class to display calendar
 */

class MonthlyCalendarBottomSheetDialog : BottomSheetDialogFragment(),
    CalendarMonthAdapter.OnMonthItemListener {

    private val binding: DialogMonthCalendarBinding by viewBinding()
    private val viewModel: StatsViewModel by sharedViewModel()
    private var lastSelectedYear: Int = 0

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
        setYearView()
    }

    private fun setupButtons() {
        lastSelectedYear = viewModel.selectedDate.year
        binding.previousYearButton.setOnClickListener {
            viewModel.selectedDate = viewModel.selectedDate.minusYears(1)
            setYearView()
        }
        binding.nextYearButton.setOnClickListener {
            viewModel.selectedDate = viewModel.selectedDate.plusYears(1)
            setYearView()
        }
    }

    /**
     * Setting Year View for daily time scale
     */
    @SuppressLint("SetTextI18n")
    private fun setYearView() {
        binding.yearTextView.text = viewModel.selectedDate.year.toString()
        val listOfMonth = DateFormatSymbols.getInstance().months.toList()
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.calendarRecyclerView.adapter =
            CalendarMonthAdapter(listOfMonth, viewModel.selectedDate, requireContext(), this, lastSelectedYear)

    }

    /**
     * Getting month and year from date
     */
    private fun monthYearFromDate(date: LocalDate?): String {
        return "${date?.month.toString().monthPrettify()} ${date?.year}"
    }

    /**
     * Processing calendar item/month clicked
     */
    override fun onItemClick(position: Int, month: Int?) {
        if (month != null) {
            viewModel.selectedDate = viewModel.selectedDate.withMonth(month+1)
            lastSelectedYear = viewModel.selectedDate.year
        }
        viewModel.selectedDate.let {

            viewModel.onDatePicked(
                if (it.monthValue > 9) "${it.year}-${it.monthValue}-${it.dayOfMonth}"
                else "${it.year}-0${it.monthValue}-${it.dayOfMonth}",
                monthYearFromDate(viewModel.selectedDate)
            )
            dismiss()
        }
    }

}