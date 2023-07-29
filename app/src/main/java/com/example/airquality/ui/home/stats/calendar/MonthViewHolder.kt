package com.example.airquality.ui.home.stats.calendar

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.airquality.databinding.MonthCellBinding
import com.example.airquality.utils.getIntForMonth

/**
 * MonthViewHolder class required to depict each item for adapter (daily)
 */

class MonthViewHolder(
    binding: MonthCellBinding,
    private val monthItemListener: CalendarMonthAdapter.OnMonthItemListener,
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    val month: TextView = binding.cellMonthText

    override fun onClick(view: View) {
        monthItemListener.onItemClick (
            absoluteAdapterPosition,
            getIntForMonth(month.text.toString())
        )
    }

    init {
        itemView.setOnClickListener(this)
    }
}