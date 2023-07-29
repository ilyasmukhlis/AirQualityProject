package com.example.airquality.utils.language

import java.util.*

/**
 * Interface for Storing Locale Setting
 */

interface LocaleStore {
    fun getLocale(): Locale
    fun persistLocale(locale: Locale)

    fun setFollowSystemLocale(value: Boolean)
    fun isFollowingSystemLocale(): Boolean
}