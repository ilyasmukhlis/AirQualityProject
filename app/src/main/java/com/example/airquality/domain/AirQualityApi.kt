package com.example.airquality.domain

import android.util.Log
import com.example.airquality.domain.`object`.LocationMeasurement
import com.example.airquality.domain.`object`.Measurements
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * AirQualityApi interface through which the requests are done
 */

interface AirQualityApi {
    @GET("indoor/devices")
    suspend fun getDevices(): List<Int>

    @GET("measurements")
    suspend fun getMeasurements(): List<Measurements>

    @GET("indoor/last")
    suspend fun getIndoorLastData(): List<Measurements>

    @GET("indoor/device")
    suspend fun getMeasurementsById(
        @Query("deviceID") deviceId: Int
    ): List<Measurements>

    @GET("indoor/month")
    suspend fun getMeasurementsMonth(
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("deviceID") deviceId: Int
    ): List<Measurements>

    @GET("indoor/stats/lastdays")
    suspend fun getMeasurementsLastDays(
        @Query("target") target: String,
        @Query("stat") stat: String,
        @Query("days") days: Int,
        @Query("deviceID") deviceId: Int
    ): List<Measurements>

    @GET("indoor/day")
    suspend fun getMeasurementsDay(
        @Query("date") date: String,
        @Query("deviceID") deviceId: Int
    ): List<Measurements>

    @GET("indoor/stats/day")
    suspend fun getMeasurementsDayStats(
        @Query("target") target: String,
        @Query("stat") stat: String,
        @Query("date") date: String,
        @Query("deviceID") deviceId: Int
    ): List<Measurements>

    @GET("outdoor")
    suspend fun getMapData(): List<LocationMeasurement>

    @GET("outdoor/last")
    suspend fun getLastOutdoorData(): List<LocationMeasurement>

    /**
     * Authorization & Logic
     */

    @POST("auth")
    suspend fun register(
        @Header("Content-Type") contentType: String,
        @Query("name") name: String,
        @Query("surname") surname: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("password") password: String,
        @Query("org") organization: String,
    ): ResponseBody

    @PUT("auth")

    suspend fun changeCredentials(
        @Header("Content-Type") contentType: String,
        @Query("id") id: String,
        @Query("name") name: String,
        @Query("surname") surname: String,
        @Query("email") email: String,
        @Query("phone") phone: String,
        @Query("password") password: String,
        @Query("org") organization: String,
    ): ResponseBody

    @GET("auth")
    suspend fun authorize(
        @Query("email") email: String,
        @Query("password") password: String
    ): ResponseBody

    @POST("auth/userdev")
    suspend fun addDeviceForUser(
        @Header("Content-Type") contentType: String,
        @Query("userID") userId: String,
        @Query("deviceID") deviceId: String
    ): ResponseBody

    @GET("outdoor/user/last")
    suspend fun getUserDevices(
        @Query("userID") userId: Int
    ): List<LocationMeasurement>

}

private const val AIR_QUALITY_API = "http://18.197.203.63:8080/airqualityback/api/"


/**
 * Retrofit Module Class, which is required to write non repetitive code via Koin Service Locator/Dependency Injection
 */

val retrofitModule = module {

    single<Gson> { GsonBuilder().setLenient().create() }
    single<Converter.Factory> { GsonConverterFactory.create(get()) }

    single<AirQualityApi> {

        val retrofit = Retrofit.Builder()
            .client(get())
            .baseUrl(AIR_QUALITY_API)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(get())
            .build()

        return@single retrofit.create(AirQualityApi::class.java)
    }

    single { getHttpClient() }

}

private fun getHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(getLoggingInterceptor())
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}


private fun getLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor {
            message -> Log.d("OkHttp", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}
