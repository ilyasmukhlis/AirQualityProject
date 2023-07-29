package com.example.airquality.ui.home.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.example.airquality.R
import com.example.airquality.data.Patterns
import com.example.airquality.databinding.ItemHomeCardBinding
import com.example.airquality.domain.`object`.MarkerData
import com.example.airquality.domain.`object`.UIEmptyItem
import com.example.airquality.utils.*
import com.example.airquality.utils.core.CoreViewHolder
import com.example.airquality.utils.core.ParameterViewHolder


/**
 *  ViewBinder for depicting the device info card
 */
class DeviceInfoCardViewBinder(
    private val onItemClick: ((MarkerData) -> Unit)
) : ItemViewBinder<MarkerData, DeviceInfoCardViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ViewHolder(inflater.inflate(R.layout.item_home_card, parent, false))

    override fun onBindViewHolder(
        holder: ViewHolder,
        item: MarkerData
    ) = holder.bind(item)

    inner class ViewHolder(itemView: View) : ParameterViewHolder<MarkerData>(itemView) {
        private val binding: ItemHomeCardBinding =
            ItemHomeCardBinding.bind(itemView)

        lateinit var localItem: MarkerData

        init {
            binding.root.setOnClickListener {
                onItemClick(localItem)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(item: MarkerData) = with(binding) {
            localItem = item
            aqiIndicationTextView.text = item.measurements.quality.toInt().toString()
            circleConstraintLayout.background.setTint(
                identifyColorByAqi(
                    item.measurements.quality.toInt(),
                    root.context
                )
            )
            aqiStatusTextView.text =
                root.context.getString(statusChecker(aqi = item.measurements.quality.toInt()))

            sensorTypeTextView.text =
                if (item.coordinates != null)
                    root.context.getString(R.string.outdoor)
                else
                    root.context.getString(R.string.indoor)

            temperatureTextView.text = "${item.measurements.temperature.toInt()}°C"
            humidityTextView.text = "${item.measurements.humidity.toInt()}%"
            dateTextView.text = convertDate(
                item.measurements.time,
                Patterns.yyyy_P_mm_P_dd_S_HH_DP_MM_DP_SS,
                Patterns.dd_D_MM_D_YYYY_S_HH_DP_MM
            )
            locationTextView.isVisible =
                item.coordinates != null || (item.distance / 1000.0).round(2) != 0.0
            locationTextView.text = "${(item.distance / 1000.0).round(2)}km"
        }
    }
}

/**
 *  ViewBinder for depicting the emptiness if the list is empty
 */

class EmptyGlobalViewBinder :
    ItemViewBinder<UIEmptyItem, EmptyGlobalViewBinder.ViewHolder>() {

    override fun onBindViewHolder(
        holder: EmptyGlobalViewBinder.ViewHolder,
        item: UIEmptyItem
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ViewHolder(inflater.inflate(R.layout.item_empty_view, parent, false))

    inner class ViewHolder(itemView: View) : CoreViewHolder<UIEmptyItem>(itemView) {
        override fun bind(item: UIEmptyItem) = with(itemView) {
            val params = layoutParams as RecyclerView.LayoutParams
            layoutParams = params.apply {
                setMargins(
                    item.left.toInt(),
                    item.top.toInt(),
                    item.right.toInt(),
                    item.bottom.toInt()
                )
            }
        }
    }
}