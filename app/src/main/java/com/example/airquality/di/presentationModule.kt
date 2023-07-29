package com.example.airquality.ui.di

import android.content.Context
import com.example.airquality.ui.home.main.HomeViewModel
import com.example.airquality.ui.home.stats.StatsViewModel
import com.example.airquality.ui.maps.MapsViewModel
import com.example.airquality.ui.usefulInfo.UsefulInfoViewModel
import com.example.airquality.utils.theme.SourcesLocalDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Presentation Module Class, which is required to write non repetitive code via Koin Service Locator/Dependency Injection
 */

val presentationModule = module {
    viewModel {
        HomeViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        StatsViewModel(
            get(),
            get()
        )
    }
    viewModel {
        MapsViewModel(
            get()
        )
    }
    viewModel {
        UsefulInfoViewModel(
            androidApplication()
        )
    }

    single {
        SourcesLocalDataSource(
            androidContext().getSharedPreferences(
                "air_quality",
                Context.MODE_PRIVATE
            )
        )
    }
}