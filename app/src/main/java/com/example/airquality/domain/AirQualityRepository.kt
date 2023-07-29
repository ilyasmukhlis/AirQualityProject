package com.example.airquality.domain

import com.example.airquality.domain.`object`.LocationMeasurement.Companion.toMarkerData
import com.example.airquality.domain.`object`.Measurements
import com.example.airquality.domain.`object`.RequestBody
import kotlinx.coroutines.flow.flow

/**
 * AirQualityRepository which implement AirQualityApi interface methods
 */

class AirQualityRepository(
    private val api: AirQualityApi
) {
    suspend fun getDevices() = flow {
        emit(api.getDevices())
    }

    suspend fun getMeasurements() = flow {
        emit(api.getMeasurements())
    }

    suspend fun getMeasurementsById(requestBody: RequestBody) = flow {
        emit(api.getMeasurementsById(requestBody.deviceId))
    }

    suspend fun getMeasurementsByIdNotFlow(requestBody: RequestBody): List<Measurements> {
        return api.getMeasurementsById(requestBody.deviceId)
    }

    suspend fun getMeasurementsMonth(requestBody: RequestBody) = flow {
        emit(
            api.getMeasurementsMonth(
                month = requestBody.month,
                year = requestBody.year,
                deviceId = requestBody.deviceId
            )
        )
    }

    suspend fun getMeasurementsLastDays(requestBody: RequestBody) = flow {
        emit(
            api.getMeasurementsLastDays(
                target = requestBody.target,
                stat = requestBody.stat,
                days = requestBody.days,
                deviceId = requestBody.deviceId
            )
        )
    }

    suspend fun getMeasurementsDayStats(requestBody: RequestBody) = flow {
        emit(
            api.getMeasurementsDayStats(
                target = requestBody.target,
                stat = requestBody.stat,
                date = requestBody.date,
                deviceId = requestBody.deviceId
            )
        )
    }

    suspend fun getMeasurementsDay(requestBody: RequestBody) = flow {
        emit(
            api.getMeasurementsDay(
                date = requestBody.date,
                deviceId = requestBody.deviceId
            )
        )

    }

    suspend fun getMarkerData() = flow {
        emit(api.getMapData().toMarkerData())
    }

    suspend fun getOutdoorLastData() = flow {
        emit(api.getLastOutdoorData().toMarkerData())
    }

    suspend fun getIndoorLastData() = flow {
        emit(api.getIndoorLastData())
    }

    suspend fun register(
        name: String = "",
        surname: String = "",
        email: String,
        phone: String = "",
        password: String,
        org: String = ""
    ) = flow {
        emit(
            api.register(
                "application/x-www-form-urlencoded",
                name,
                surname,
                email,
                phone,
                password,
                org
            )
        )
    }

    suspend fun changeCredentials(
        id: String,
        name: String = "",
        surname: String = "",
        email: String,
        phone: String = "",
        password: String,
        org: String = ""
    ) = flow {
        emit(
            api.changeCredentials(
                "application/x-www-form-urlencoded",
                id,
                name,
                surname,
                email,
                phone,
                password,
                org
            )
        )
    }

    suspend fun authorize(
        email: String,
        password: String
    ) = flow {
        emit(
            api.authorize(
                email,
                password
            )
        )
    }

    suspend fun addDeviceForUser(
        userId: String,
        deviceId: String
    ) = flow {
        emit(
            api.addDeviceForUser(
                "application/x-www-form-urlencoded",
                userId,
                deviceId
            )
        )
    }

    suspend fun getDevicesForUser(
        userId: Int,
    ) = flow {
        emit(
            api.getUserDevices(
                userId
            )
        )
    }
}