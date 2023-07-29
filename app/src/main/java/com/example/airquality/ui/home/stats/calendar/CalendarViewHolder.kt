package com.example.airquality.ui.home.stats.calendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.airquality.databinding.CalendarCellBinding

/**
 * CalendarViewHolder class required to depict each item for adapter (hourly)
 */

class CalendarViewHolder(
    binding: CalendarCellBinding,
    private val onCalendarItemListener: CalendarAdapter.OnCalendarItemListener
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    val dayOfMonth: TextView = binding.cellDayText

    override fun onClick(view: View) {
        onCalendarItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }

    init {
        itemView.setOnClickListener(this)
    }
}