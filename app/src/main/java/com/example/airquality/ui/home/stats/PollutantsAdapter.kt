package com.example.airquality.ui.home.stats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.airquality.R
import com.example.airquality.databinding.PollutantsItemBinding
import com.example.airquality.domain.`object`.Pollutant
import com.example.airquality.utils.core.OnItemClickListener

/**
 * PollutantsAdapter class required to fit list of specific data into view (pollutants)
 */

class PollutantsAdapter(
    private val listener: OnItemClickListener<Pollutant>
) : ListAdapter<Pollutant, PollutantViewHolder>(PollutantDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollutantViewHolder {
        return PollutantViewHolder(
            PollutantsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }


    override fun onBindViewHolder(holder: PollutantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/**
 * PollutantViewHolder class required to depict each item for adapter (pollutants)
 */

class PollutantViewHolder(
    val binding: PollutantsItemBinding,
    private val listener: OnItemClickListener<Pollutant>
) : RecyclerView.ViewHolder(binding.root) {

    private var selected: Pollutant? = null

    init {
        binding.root.setOnClickListener {
            selected?.let {
                listener.onItemClick(it, absoluteAdapterPosition)
            }
        }
    }

    /**
     * setting pollutant as selected
     */

    private fun setSelected() = with(binding) {
        pollutantTextView.setTextColor(root.context.getColor(R.color.whiteStable))
        pollutantCardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorFill1Stable))
    }

    /**
     * setting pollutant as unselected
     */

    private fun setUnselected() = with(binding) {
        pollutantTextView.setTextColor(root.context.getColor(R.color.colorFill1Stable))
        pollutantCardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.whiteStable))
    }

    fun bind(item: Pollutant) {
        this.selected = item
        binding.pollutantTextView.text = item.title
        binding.pollutantTextView.isChecked = item.isSelected!!
        if (binding.pollutantTextView.isChecked) setSelected() else setUnselected()
    }

}

/**
 * Basic comparator for adding only unique pollutants
 */

object PollutantDiff : DiffUtil.ItemCallback<Pollutant>() {

    override fun areItemsTheSame(oldItem: Pollutant, newItem: Pollutant): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Pollutant, newItem: Pollutant): Boolean {
        return oldItem.title == newItem.title
    }
}