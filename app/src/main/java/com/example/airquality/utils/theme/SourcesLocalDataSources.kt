package com.example.airquality.utils.theme

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Default implementation of ThemeDataSource using [SharedPreferences].
 */

class SourcesLocalDataSource(private val pref: SharedPreferences) {

    private val PREF_THEME = "PREF_USER_THEME"

    /**
     *  Set Theme
     */
    fun setTheme(theme: Theme) = pref.edit {
        putString(PREF_THEME, theme.name)
    }

    /**
     * Get Theme
     */
    fun getTheme(defaultValue: String = "") = pref.getString(PREF_THEME, defaultValue)
}
