package com.example.airquality.ui.home.stats.calendar

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.airquality.R
import com.example.airquality.databinding.CalendarCellBinding
import java.time.LocalDate

/**
 * CalendarAdapter class required to fit list of specific data into view (hourly)
 */
@RequiresApi(Build.VERSION_CODES.O)
class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val selectedDate: LocalDate,
    private val context:Context,
    private val onCalendarItemListener: OnCalendarItemListener,
    private val lastSelectedMonth: Int
) : RecyclerView.Adapter<CalendarViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarCellBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(binding, onCalendarItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonth[position]
        if (selectedDate.dayOfMonth.toString() == daysOfMonth[position] && selectedDate.month.value == lastSelectedMonth) {
            holder.dayOfMonth.setTextColor(ContextCompat.getColor(context, R.color.colorFillRedOrange))
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    /**
     * OnCalendarItemListener Interface to catch which element was clicked and pass relevant data
     */

    interface OnCalendarItemListener {
        fun onItemClick(position: Int, dayText: String?)
    }
}
