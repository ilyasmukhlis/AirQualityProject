package com.example.airquality.domain

import com.example.airquality.domain.`object`.*
import com.example.airquality.utils.core.CoreLaunchUseCase
import com.example.airquality.utils.core.CoreNonParamLaunchUseCase
import com.example.airquality.utils.core.StringDateComparator
import com.google.android.gms.maps.model.LatLng
import java.util.*


/**
 * Getting All Outdoor Devices Data
 */
class GetOutdoorDevicesUseCase(
    private val repo: AirQualityRepository
) : CoreNonParamLaunchUseCase<List<MarkerData>>() {
    override fun execute(result: (List<MarkerData>) -> Unit) {
        launchFlow({
            repo.getMarkerData()
        }, {
            result(transformObject(it))
        })
    }

    private fun transformObject(data: List<MarkerData>): List<MarkerData> {
        val markers = mutableListOf<MarkerData>().apply {
            data.distinctBy { Pair(it.measurements.deviceId, it.coordinates) }.forEach {
                add(it)
            }
        }.also {
            Collections.sort(it, StringDateComparator())
        }

        return markers
    }
}

/**
 * Getting Last (recent) Outdoor Devices Data
 */
class GetLastOutdoorDevicesUseCase(
    private val repo: AirQualityRepository
) : CoreNonParamLaunchUseCase<List<MarkerData>>() {
    override fun execute(result: (List<MarkerData>) -> Unit) {
        launchFlow({
            repo.getOutdoorLastData()
        }, {
            result(transformObject(it))
        })
    }

    private fun transformObject(data: List<MarkerData>): List<MarkerData> {
        val markers = mutableListOf<MarkerData>().apply {
            data.distinctBy { Pair(it.measurements.deviceId, it.coordinates) }
                .filter { it.coordinates?.latitude != 0.0 && it.coordinates?.longitude != 0.0 }
                .sortedByDescending { it.measurements.time }
                .forEach {
                    add(it)
                }
        }

        return markers
    }
}

/**
 * Getting Available Device IDs
 */
class GetDevicesUseCase(
    private val repo: AirQualityRepository
) : CoreNonParamLaunchUseCase<List<Int>>() {
    override fun execute(result: (List<Int>) -> Unit) {
        launchFlow({
            repo.getDevices()
        }, {
            result(it)
        })
    }
}

/**
 * Getting All Indoor Devices Data
 */
class GetIndoorMeasurementsUseCase(
    private val repo: AirQualityRepository
) : CoreNonParamLaunchUseCase<List<Measurements>>() {

    override fun execute(result: (List<Measurements>) -> Unit) {
        launchFlow({
            repo.getMeasurements()
        }, {
            result(it)
        })
    }
}

/**
 * Getting Last (recent) Indoor Devices Data
 */
class GetLastIndoorDevicesUseCase(
    private val repo: AirQualityRepository
) : CoreNonParamLaunchUseCase<List<Measurements>>() {

    override fun execute(result: (List<Measurements>) -> Unit) {
        launchFlow({
            repo.getIndoorLastData()
        }, {
            result(it)
        })
    }
}

/**
 * Getting Collection of Device by its ID
 */
class GetIndoorMeasurementByIdUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<Int, List<Measurements>>() {

    override fun execute(param: Int, result: (List<Measurements>) -> Unit) {
        launchFlow({
            repo.getMeasurementsById(RequestBody(deviceId = param))
        }, {
            result(it)
        })
    }
}

/**
 * Getting Month Data for Device ID
 */
class GetMonthMeasurementsMonthUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<RequestBody, List<Measurements>>() {
    override fun execute(param: RequestBody, result: (List<Measurements>) -> Unit) {
        launchFlow({
            repo.getMeasurementsMonth(param)
        }, {
            result(it)
        })
    }
}

/**
 * Getting Last (recent) for Device ID
 */
class GetLastDaysMeasurementsUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<RequestBody, List<Measurements>>() {
    override fun execute(param: RequestBody, result: (List<Measurements>) -> Unit) {
        launchFlow({
            repo.getMeasurementsLastDays(param)
        }, {
            result(it)
        })
    }
}

/**
 * Getting Day Data for Device ID with statistic tools
 */

class GetDayMeasurementStatsUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<RequestBody, List<Measurements>>() {
    override fun execute(param: RequestBody, result: (List<Measurements>) -> Unit) {
        launchFlow({
            repo.getMeasurementsDayStats(param)
        }, {
            result(it)
        })
    }
}

/**
 * Getting Day Data for Device ID
 */
class GetDayMeasurementsUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<RequestBody, List<Measurements>>() {
    override fun execute(param: RequestBody, result: (List<Measurements>) -> Unit) {
        launchFlow({
            repo.getMeasurementsDay(param)
        }, {
            result(it)
        })
    }
}

/**
 * Registration case
 */
class RegisterUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<LoginBody, String>() {
    override fun execute(param: LoginBody, result: (String) -> Unit) {
        launchFlow({
            repo.register(
                name = param.name,
                surname = param.surname,
                email = param.email,
                phone = param.phone,
                password = param.password,
                org = param.org
            )
        }, {
            result(it.string())
        })

    }
}

/**
 * Updating User data
 */
class ChangeUserDataUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<LoginBody, String>() {
    override fun execute(param: LoginBody, result: (String) -> Unit) {
        launchFlow({
            repo.changeCredentials(
                id = param.id,
                name = param.name,
                surname = param.surname,
                email = param.email,
                phone = param.phone,
                password = param.password,
                org = param.org
            )
        }, {
            result(it.string())
        })
    }
}

/**
 * Authorization case
 */
class LoginUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<Pair<String, String>, String>() {
    override fun execute(param: Pair<String, String>, result: (String) -> Unit) {
        val (email, passwd) = param
        launchFlow({
            repo.authorize(
                email = email,
                password = passwd
            )
        }, {
            result(it.string())
        })
    }
}

/**
 * Adding device for certain user
 */

class AddDeviceUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<Pair<String, String>, String>() {
    override fun execute(param: Pair<String, String>, result: (String) -> Unit) {
        val (userId, deviceId) = param
        launchFlow({
            repo.addDeviceForUser(
                userId = userId,
                deviceId = deviceId
            )
        }, {
            result(it.string())
        })
    }
}

class GetDevicesForUserUseCase(
    private val repo: AirQualityRepository
) : CoreLaunchUseCase<Int, List<MarkerData>>() {

    override fun execute(param: Int, result: (List<MarkerData>) -> Unit) {
        launchFlow({
            repo.getDevicesForUser(param)
        }, {
            result(transformObject(it))
        })
    }

    private fun transformObject(data: List<LocationMeasurement>) =
        mutableListOf<MarkerData>().apply {
            data.map {
                add(
                    MarkerData(
                        measurements = Measurements(
                            ch4 = it.ch4,
                            co = it.co,
                            h2s = it.h2s,
                            nox = it.nox,
                            o3 = it.o3,
                            pm25 = it.pm25,
                            quality = it.quality,
                            temperature = it.temperature,
                            humidity = it.humidity,
                            deviceId = it.deviceId,
                            time = it.time
                        ),
                        coordinates = if (it.latitude != 0.0 && it.longitude != 0.0)
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        else null
                    )
                )
            }
        }
}
