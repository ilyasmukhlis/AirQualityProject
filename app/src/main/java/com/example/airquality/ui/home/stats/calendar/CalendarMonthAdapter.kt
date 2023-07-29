package com.example.airquality.ui.home.stats.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.airquality.R
import com.example.airquality.databinding.MonthCellBinding
import java.time.LocalDate

/**
 * CalendarAdapter class required to fit list of specific data into view (daily)
 */
class CalendarMonthAdapter(
    private val months: List<String>,
    private val selectedDate: LocalDate,
    private val context: Context,
    private val onMonthItemListener: OnMonthItemListener,
    private val lastSelectedYear: Int
) : RecyclerView.Adapter<MonthViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding = MonthCellBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams.height = (parent.height * 0.25).toInt()
        return MonthViewHolder(binding, onMonthItemListener)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val date = months[position]
        holder.month.text = date

        if (selectedDate.month.name.equals(date, ignoreCase = true) && lastSelectedYear == selectedDate.year) {
            holder.month.setTextColor(ContextCompat.getColor(context, R.color.colorFillRed))
        }
    }

    override fun getItemCount(): Int {
        return months.size
    }

    /**
     * OnMonthItemListener Interface to catch which element was clicked and pass relevant data
     */

    interface OnMonthItemListener {
        fun onItemClick(position: Int, month: Int?)
    }
}