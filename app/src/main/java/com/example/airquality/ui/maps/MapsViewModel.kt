package com.example.airquality.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.airquality.domain.GetLastOutdoorDevicesUseCase
import com.example.airquality.domain.`object`.MarkerData


/**
 * MapsViewModel class responsible for all of the logic in [MapsFragment]
 */
class MapsViewModel(
    private val getLastOutdoorDevicesUseCase: GetLastOutdoorDevicesUseCase
) : ViewModel() {

    var refinedMarkerData = listOf<MarkerData>()
    private val _markers = MutableLiveData<List<MarkerData>>()
    val markers: LiveData<List<MarkerData>> = _markers

    /**
     *  Method used for getting outdoor devices data
     */
    fun getMapMarkerData() {
        try {
            getLastOutdoorDevicesUseCase.execute {
                refinedMarkerData = it
                _markers.value = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}