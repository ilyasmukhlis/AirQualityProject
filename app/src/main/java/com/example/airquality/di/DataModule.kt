package com.example.airquality.di

import com.example.airquality.domain.*
import org.koin.dsl.module

/**
 * Data Module Class, which is required to write non repetitive code via Koin Service Locator/Dependency Injection
 */

val dataModule = module {
    factory {
        GetOutdoorDevicesUseCase(
            get()
        )
    }
    factory {
        GetDevicesUseCase(
            get()
        )
    }
    factory {
        GetIndoorMeasurementsUseCase(
            get()
        )
    }
    factory {
        GetIndoorMeasurementByIdUseCase(
            get()
        )
    }
    factory {
        GetMonthMeasurementsMonthUseCase(
            get()
        )
    }
    factory {
        GetLastDaysMeasurementsUseCase(
            get()
        )
    }
    factory {
        GetDayMeasurementStatsUseCase(
            get()
        )
    }
    factory {
        GetDayMeasurementsUseCase(
            get()
        )
    }
    factory {
        AirQualityRepository(
            get()
        )
    }

    factory {
        GetLastOutdoorDevicesUseCase(
            get()
        )
    }

    factory {
        GetLastIndoorDevicesUseCase(
            get()
        )
    }
    factory {
        RegisterUseCase(
            get()
        )
    }
    factory {
        ChangeUserDataUseCase(
            get()
        )
    }
    factory {
        LoginUseCase(
            get()
        )
    }
    factory {
        AddDeviceUseCase(
            get()
        )
    }
    factory {
        GetDevicesForUserUseCase(
            get()
        )
    }
}