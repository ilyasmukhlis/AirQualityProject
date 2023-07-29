package com.example.airquality.ui.home.stats

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airquality.domain.GetDayMeasurementsUseCase
import com.example.airquality.domain.GetLastDaysMeasurementsUseCase
import com.example.airquality.domain.`object`.Measurements
import com.example.airquality.domain.`object`.RequestBody
import com.example.airquality.ui.home.stats.StatsDialog.Companion.DEVICE_ID
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * StatsViewModel class responsible for all of the logic in [StatsDialog]
 */

class StatsViewModel(
    private val getLastDaysMeasurementsUseCase: GetLastDaysMeasurementsUseCase,
    private val getDayMeasurementsUseCase: GetDayMeasurementsUseCase
) : ViewModel() {


    private val currentTime: LocalDateTime = LocalDateTime.now()

    private val _measurements = MutableLiveData<List<Measurements>>()
    val measurements: LiveData<List<Measurements>> = _measurements

    private val _measurementsMonth = MutableLiveData<List<Measurements>>()
    val measurementsMonth: LiveData<List<Measurements>> = _measurementsMonth

    private val _measurementsDay = MutableLiveData<List<Measurements>>()
    val measurementsDay: LiveData<List<Measurements>> = _measurementsDay

    private val _pickedDate = MutableLiveData<Pair<String?, String?>>()
    val pickedDate: LiveData<Pair<String?, String?>> = _pickedDate

    private val _setTitle = MutableLiveData<String>()
    val setTitle: LiveData<String> = _setTitle

    var selectedDate: LocalDate = LocalDate.now()

    var deviceId: String = ""

    fun onCreate(args: Bundle) {
        deviceId = args.getString(DEVICE_ID)!!
    }

    fun onViewCreated() {
        _setTitle.value = deviceId
    }

    fun onDatePicked(date: String?, buttonText: String?) {
        _pickedDate.postValue(Pair(date, buttonText))
    }

    fun getMeasurementsLastDays(days: Int) {
        // TODO FIX LOGIC
        try {
            if (days > 30) {
                var largerList = listOf<Measurements>()
                var smallerList = listOf<Measurements>()
                val paramLarge = RequestBody(
                    stat = "mean",
                    days = days,
                    deviceId = deviceId.toInt()
                )
                val paramSmall = RequestBody(
                    stat = "mean",
                    days = days - (days - 30),
                    deviceId = deviceId.toInt()
                )
                getLastDaysMeasurementsUseCase.execute(paramLarge) {
                    largerList = it
                }
                getLastDaysMeasurementsUseCase.execute(paramSmall) {
                    smallerList = it
                    _measurementsMonth.value = largerList.filter { it !in smallerList }
                }


            } else {
                val param = RequestBody(
                    stat = "mean",
                    days = days,
                    deviceId = deviceId.toInt()
                )

                getLastDaysMeasurementsUseCase.execute(param) {
                    _measurementsMonth.value = it
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMeasurementsDayStats(date: String?) {
        val param = RequestBody(
            date = date ?: fetchFullDate(),
            deviceId = deviceId.toInt()
        )
        try {
            getDayMeasurementsUseCase.execute(param) {
                _measurementsDay.value = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fetchFullDate(): String {
        return currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}