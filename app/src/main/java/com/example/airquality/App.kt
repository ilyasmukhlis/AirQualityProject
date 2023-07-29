package com.example.airquality

import android.app.Application
import com.example.airquality.domain.`object`.MarkerData
import com.example.airquality.domain.`object`.Measurements
import com.example.airquality.di.dataModule
import com.example.airquality.domain.retrofitModule
import com.example.airquality.ui.di.presentationModule
import com.example.airquality.utils.initLanguage
import com.google.android.gms.maps.model.LatLng
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * The main App Class responsible for caring the application and where dependency among modules is initiated
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, retrofitModule, presentationModule)
        }
        initLanguage()
    }

    /**
     * companion object to store mock data
     */

    companion object {
        //Mock Data
        val dormData = MarkerData(
            measurements = Measurements(
                ch4 = 15.0,
                co = 70.0,
                h2s = 8.0,
                pm25 = 13.0,
                nox = 22.0,
                o3 = 43.0,
                deviceId = 40,
                humidity = 35.0,
                temperature = 25.0,
                quality = 82.0,
                time = "2022-09-24 13:46:41"
            ),
            location = "Astana",
            coordinates = LatLng(51.088346, 71.395998)
        )

        val labDevice1 = MarkerData(
            measurements = Measurements(
                ch4 = 32.0,
                co = 79.0,
                h2s = 18.0,
                pm25 = 14.0,
                nox = 22.0,
                o3 = 95.0,
                deviceId = 50,
                humidity = 30.0,
                temperature = 20.0,
                quality = 68.0,
                time = "2022-09-25 13:56:48"
            ),
            location = "Astana",
            coordinates = LatLng(51.092167, 71.397316)
        )

        val labDevice2 = MarkerData(
            measurements = Measurements(
                ch4 = 33.0,
                co = 76.0,
                h2s = 16.0,
                pm25 = 14.0,
                nox = 24.0,
                o3 = 95.0,
                deviceId = 51,
                humidity = 30.0,
                temperature = 20.0,
                quality = 75.0,
                time = "2022-09-24 12:53:32"
            ),
            location = "Astana",
            coordinates = LatLng(51.092135, 71.397391)
        )

        val exampleDevice1 = MarkerData(
            measurements = Measurements(
                ch4 = 33.0,
                co = 30.0,
                h2s = 16.0,
                pm25 = 14.0,
                nox = 24.0,
                o3 = 20.0,
                deviceId = 60,
                humidity = 30.0,
                temperature = 20.0,
                quality = 30.0,
                time = "2022-09-25 12:33:43"
            ),
            location = "Astana",
            coordinates = LatLng(51.090858, 71.394985)
        )

        val exampleDevice2 = MarkerData(
            measurements = Measurements(
                ch4 = 33.0,
                co = 76.0,
                h2s = 16.0,
                pm25 = 14.0,
                nox = 24.0,
                o3 = 95.0,
                deviceId = 61,
                humidity = 30.0,
                temperature = 20.0,
                quality = 153.0,
                time = "2022-09-24 11:42:19"
            ),
            location = "Astana",
            coordinates = LatLng(51.091239, 71.392118)
        )

        val exampleDevice3 = MarkerData(
            measurements = Measurements(
                ch4 = 33.0,
                co = 76.0,
                h2s = 16.0,
                pm25 = 14.0,
                nox = 24.0,
                o3 = 95.0,
                deviceId = 72,
                humidity = 30.0,
                temperature = 20.0,
                quality = 153.0,
                time = "2022-09-24 11:42:19"
            ),
            location = "Indoor",
            coordinates = null
        )

        val exampleDevice4 = MarkerData(
            measurements = Measurements(
                ch4 = 3.0,
                co = 7.0,
                h2s = 1.0,
                pm25 = 10.0,
                nox = 24.0,
                o3 = 4.0,
                deviceId = 74,
                humidity = 30.0,
                temperature = 20.0,
                quality = 21.0,
                time = "2022-09-24 11:42:19"
            ),
            location = "Indoor",
            coordinates = null
        )

        val exampleDevice5 = MarkerData(
            measurements = Measurements(
                ch4 = 33.0,
                co = 76.0,
                h2s = 16.0,
                pm25 = 14.0,
                nox = 24.0,
                o3 = 95.0,
                deviceId = 80,
                humidity = 30.0,
                temperature = 20.0,
                quality = 32.0,
                time = "2022-09-24 11:42:19"
            ),
            location = "Indoor",
            coordinates = null
        )
        val deviceList = listOf(80, 74, 72, 50, 51, 60, 61, 40)
        val indoor = listOf(exampleDevice3, exampleDevice4, exampleDevice5)
        val mapMeasurements = listOf(dormData, labDevice1, labDevice2, exampleDevice1, exampleDevice2)
    }

}