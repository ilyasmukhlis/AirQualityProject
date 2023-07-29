package com.example.airquality.utils.theme


import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


/**
 * The delegate is used to define the theme and change the theme of the application
 */
interface DarkTheme {

    /**
     * Init App Theme
     */
    fun initTheme(window: Window, defaultTheme: Theme)

    /**
     * Set App Theme
     */
    fun setTheme(theme: Theme, window: Window)

    /**
     *  Get App Theme
     */
    fun getUITheme(): Theme
}


class DarkThemeDelegate : DarkTheme, KoinComponent {


    private val sourcesLocalStorage by inject<SourcesLocalDataSource>()

    override fun initTheme(window: Window, defaultTheme: Theme) {
        val theme = sourcesLocalStorage.getTheme(Theme.SYSTEM.name) ?: defaultTheme.name
        makeTheme(theme, window)
    }

    override fun setTheme(theme: Theme, window: Window) {
        sourcesLocalStorage.setTheme(theme)
        makeTheme(sourcesLocalStorage.getTheme(Theme.SYSTEM.name), window)
    }

    override fun getUITheme(): Theme {
        return Theme.valueOf(sourcesLocalStorage.getTheme(Theme.SYSTEM.name).orEmpty())
    }


    private fun makeTheme(theme: String?, window: Window) {
        when (theme) {
            Theme.DARK.name -> makeDarkTheme(window)
            Theme.APP.name -> makeLightTheme(window)
            Theme.SYSTEM.name -> makeSystemTheme(window)
        }
    }

    /**
     *  Sets Dark Theme
     */

    private fun makeDarkTheme(window: Window) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    /**
     *  Sets Light Theme
     */

    private fun makeLightTheme(window: Window) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    /**
     *  Sets/Follows System Theme
     */

    private fun makeSystemTheme(window: Window) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

}

enum class Theme {
    DARK, APP, SYSTEM
}


