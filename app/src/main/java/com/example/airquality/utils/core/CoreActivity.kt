package com.example.airquality.utils.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.airquality.utils.theme.DarkTheme
import com.example.airquality.utils.theme.DarkThemeDelegate
import com.example.airquality.utils.theme.Theme


/**
 * The CoreActivity Class created for maintaing the app theme
 */

abstract class CoreActivity : AppCompatActivity(), DarkTheme by DarkThemeDelegate()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme(window, Theme.DARK)
        super.onCreate(savedInstanceState)
        }
}