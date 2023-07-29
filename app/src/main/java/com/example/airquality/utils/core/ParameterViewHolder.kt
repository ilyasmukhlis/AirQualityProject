package com.example.airquality.utils.core

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize


/**
 * ViewHolder used to bind item to its view inside ViewBinder
 */

open class ParameterViewHolder<T : BaseGlobalLayoutParameter>(itemView: View) :
    CoreViewHolder<T>(itemView) {
    override fun bind(item: T) = with(itemView) {
        if(rootView.layoutParams is RecyclerView.LayoutParams) {
            val lp = rootView.layoutParams as RecyclerView.LayoutParams
            rootView.layoutParams = lp.apply {
                setMargins(
                    item.left.toInt(),
                    item.top.toInt(),
                    item.right.toInt(),
                    item.bottom.toInt()
                )

                setPadding(
                    item.paddingLeft?.toInt() ?: paddingLeft ?: 0,
                    item.paddingTop?.toInt() ?: paddingTop ?: 0,
                    item.paddingRight?.toInt() ?: paddingRight ?: 0,
                    item.paddingBottom?.toInt() ?: paddingBottom ?: 0
                )
            }
        }
    }
}

/**
 * Base item data class
 */

@Parcelize
open class BaseGlobalLayoutParameter : Parcelable {
    var left: Float = 0f
    var top: Float = 0f
    var right: Float = 0f
    var bottom: Float = 0f

    var paddingLeft: Float? = null
    var paddingTop: Float? = null
    var paddingRight: Float? = null
    var paddingBottom: Float? = null
}

/**
 * CoreViewHolder used to bind item to its view inside ViewBinder
 */

abstract class CoreViewHolder<V>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: V)
}