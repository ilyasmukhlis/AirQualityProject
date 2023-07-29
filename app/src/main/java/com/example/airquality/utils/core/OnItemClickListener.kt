package com.example.airquality.utils.core

/**
 * General Interface for processing item/day/month click
 */
interface OnItemClickListener<T> {
    fun onItemClick(item: T, position: Int)
}