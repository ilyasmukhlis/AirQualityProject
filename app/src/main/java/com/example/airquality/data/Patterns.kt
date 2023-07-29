package com.example.airquality.data


/**
 * Date Patterns
 */

object Patterns {
    /**
     * Показываем месяц день и время
     * Нэйминг для формата дат
     * S - пробел
     * С - запятая
     * P - divider
     * D - dot
     * DP - двоеточие
     */
    const val yyyy_P_mm_P_dd_S_HH_DP_MM_DP_SS = "yyyy-MM-dd HH:mm:ss"
    const val yyyy_D_mm_D_dd_S_HH_DP_MM_DP_SS = "yyyy.mm.dd HH:mm:ss"
    const val yyyy_P_mm_P_dd = "yyyy-mm-dd"
    const val HH_DP_MM_DP_SS = "HH:mm:ss"
    const val dd_S_MMMM_C_S_YYYY = "dd MMMM, yyyy"
    const val dd_D_MM_D_YYYY_S_HH_DP_MM = "dd.MM.yyyy HH:mm"

}