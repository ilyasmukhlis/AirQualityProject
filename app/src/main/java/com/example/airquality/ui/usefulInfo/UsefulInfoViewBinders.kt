package com.example.airquality.ui.usefulInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drakeet.multitype.ItemViewBinder
import com.example.airquality.R
import com.example.airquality.databinding.ItemInfoCardBinding
import com.example.airquality.domain.`object`.InfoCardData
import com.example.airquality.utils.core.ParameterViewHolder

/**
 *  ViewBinder for depicting the useful info cards
 */

class InfoCardViewBinder(
    private val onItemClick: ((InfoCardData) -> Unit)
) : ItemViewBinder<InfoCardData, InfoCardViewBinder.ViewHolder>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ViewHolder(inflater.inflate(R.layout.item_info_card, parent, false))

    override fun onBindViewHolder(
        holder: InfoCardViewBinder.ViewHolder,
        item: InfoCardData
    ) = holder.bind(item)

    inner class ViewHolder(itemView: View) : ParameterViewHolder<InfoCardData>(itemView) {
        val binding: ItemInfoCardBinding = ItemInfoCardBinding.bind(itemView)
        lateinit var localItem: InfoCardData

        init {
            binding.root.setOnClickListener {
                onItemClick(localItem)
            }
        }

        override fun bind(item: InfoCardData) = with(binding) {
            localItem = item
            titleTextView.text = item.title
            shortDescriptionTextView.text = item.description
        }
    }

}