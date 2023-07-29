package com.example.airquality.domain.`object`

import android.os.Parcelable
import com.example.airquality.utils.core.BaseGlobalLayoutParameter
import com.example.airquality.utils.round
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs

/**
 * Data class used for measurement data
 */

@Parcelize
data class Measurements(
    @SerializedName("CH4") val ch4: Double,
    @SerializedName("CO") val co: Double,
    @SerializedName("H2S") val h2s: Double,
    @SerializedName("NOx") val nox: Double,
    @SerializedName("O3") val o3: Double,
    @SerializedName("PM25") val pm25: Double,
    @SerializedName("deviceID") val deviceId: Int,
    @SerializedName("humidity") val humidity: Double,
    @SerializedName("quality") val quality: Double,
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("time") val time: String
) : Parcelable {

    companion object {
        private fun randDoubleGen(): Double {
            return ThreadLocalRandom.current().nextDouble(-60.0, 60.0)
        }

        private fun randTempGen(): Double {
            return ThreadLocalRandom.current().nextDouble(-8.0, 5.0)
        }

        private fun randAqiGen(): Int {
            return ThreadLocalRandom.current().nextInt(102, 130)
        }

        fun hourlyMock(): Measurements {
            val ch4 = abs(73.0 + randDoubleGen()).round(1)
            val co = abs(110.0 + randDoubleGen()).round(1)
            val h2s = abs(89.0 + randDoubleGen()).round(1)
            val pm25 = abs(65.0 + randDoubleGen()).round(1)
            val nox = abs(65.0 + randDoubleGen()).round(1)
            val o3 = abs(36.0 + randDoubleGen()).round(1)
            val temperature = abs(8 + randTempGen()).round(1)
            val quality = when (((ch4 + co + h2s + pm25 + nox + o3) / 6).toInt()) {
                in 0..15 -> 8
                in 21..29 -> 24
                in 30..39 -> 37
                in 40..49 -> 45
                in 50..59 -> 58
                in 60..65 -> 72
                in 65..69 -> 78
                in 70..73 -> 82
                in 74..76 -> 85
                in 77..79 -> 87
                in 80..83 -> 93
                in 84..86 -> 96
                in 87..89 -> 98
                else -> randAqiGen()
            }

            return Measurements(
                ch4,
                co,
                h2s,
                pm25,
                nox,
                o3,
                deviceId = 2,
                humidity = 35.0,
                temperature = temperature,
                quality = quality.toDouble(),
                time = "2021-09-17 05:56:48"
            )
        }

        fun dailyMock(): Measurements {
            val ch4 = abs(73.0 + randDoubleGen())
            val co = abs(110.0 + randDoubleGen())
            val h2s = abs(89.0 + randDoubleGen())
            val pm25 = abs(65.0 + randDoubleGen())
            val nox = abs(65.0 + randDoubleGen())
            val o3 = abs(36.0 + randDoubleGen())
            val temperature = abs(8 + randTempGen())
            val quality = when (((ch4 + co + h2s + pm25 + nox + o3) / 6).toInt()) {
                in 0..15 -> 8
                in 21..29 -> 24
                in 30..39 -> 37
                in 40..49 -> 45
                in 50..59 -> 58
                in 60..65 -> 72
                in 65..69 -> 78
                in 70..73 -> 82
                in 74..76 -> 85
                in 77..79 -> 87
                in 80..83 -> 93
                in 84..86 -> 96
                in 87..89 -> 98
                else -> randAqiGen()
            }

            return Measurements(
                ch4,
                co,
                h2s,
                pm25,
                nox,
                o3,
                deviceId = 2,
                humidity = 35.0,
                temperature = temperature,
                quality = quality.toDouble(),
                time = "2021-09-17 05:56:48"
            )
        }
    }
}

/**
 * Data class used for measurement data + location
 */

@Parcelize
data class LocationMeasurement(
    @SerializedName("CH4") val ch4: Double,
    @SerializedName("CO") val co: Double,
    @SerializedName("H2S") val h2s: Double,
    @SerializedName("NOx") val nox: Double,
    @SerializedName("O3") val o3: Double,
    @SerializedName("PM25") val pm25: Double,
    @SerializedName("deviceID") val deviceId: Int,
    @SerializedName("humidity") val humidity: Double,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("longd") val longitude: Double,
    @SerializedName("quality") val quality: Double,
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("time") val time: String
) : Parcelable {
    companion object {
        fun List<LocationMeasurement>.toMarkerData(): List<MarkerData> {
            return this.map {
                MarkerData(
                    Measurements(
                        it.ch4.round(2),
                        it.co.round(2),
                        it.h2s.round(2),
                        it.nox.round(2),
                        it.o3.round(2),
                        it.pm25.round(2),
                        it.deviceId,
                        it.humidity,
                        it.quality,
                        it.temperature,
                        it.time
                    ),
                    "N/A",
                    LatLng(it.latitude, it.longitude)
                )
            }.toList() ?: emptyList()
        }
    }
}

/**
 * Data class used for setting marker data
 */

@Parcelize
data class MarkerData(
    val measurements: Measurements,
    val location: String = "",
    val coordinates: LatLng? = null,
    val distance: Double = 0.0
) : Parcelable, BaseGlobalLayoutParameter()

/**
 * Data class used for depicting pollutant
 */

data class Pollutant(
    val title: String,
    val enum: PollutantEnum? = null,
    var isSelected: Boolean? = false
)

/**
 * enum class used for pollutant typification
 */
enum class PollutantEnum {
    H2S,
    CO,
    CH4,
    PM25,
    NO2,
    O3,
    AQI
}

/**
 * Data class used for Info Cards
 */

@Parcelize
data class InfoCardData(
    val title: String,
    val description: String
) : BaseGlobalLayoutParameter()

/**
 * Data class used for measurement stats data request
 */

data class RequestBody(
    val deviceId: Int = 0,
    val stat: String = "",
    val target: String = "",
    val date: String = "",
    val month: Int = 1,
    val year: Int = 2022,
    val days: Int = 30
)

/**
 * Data class used for login/registration process
 */

data class LoginBody(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String,
    val phone: String = "",
    val password: String,
    val org: String = ""
)

/**
 * UI model for adding margins
 * [EmptyGlobalViewBinder]
 */
@kotlinx.android.parcel.Parcelize
class UIEmptyItem : BaseGlobalLayoutParameter()


