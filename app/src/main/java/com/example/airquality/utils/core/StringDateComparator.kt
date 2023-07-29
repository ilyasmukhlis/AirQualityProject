package com.example.airquality.utils.core

import com.example.airquality.data.Patterns
import com.example.airquality.domain.`object`.MarkerData
import java.text.SimpleDateFormat

/**
 * StringDateComparator used for comparing dates
 */

class StringDateComparator: Comparator<MarkerData> {
    val dateFormat = SimpleDateFormat(Patterns.yyyy_P_mm_P_dd_S_HH_DP_MM_DP_SS)

    override fun compare(p0: MarkerData?, p1: MarkerData?): Int {
        return dateFormat.parse(p0?.measurements?.time.toString())?.compareTo(dateFormat.parse(p1?.measurements?.time.toString())) ?: -1
    }
}

