package com.example.airquality.ui.home.main

import android.location.Location
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airquality.domain.*
import com.example.airquality.domain.`object`.*
import com.example.airquality.utils.EventWrapper
import com.example.airquality.utils.round
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * HomeViewModel class responsible for all of the logic in [HomeFragment]
 */

class HomeViewModel(
    private val getLastOutdoorDevicesUseCase: GetLastOutdoorDevicesUseCase,
    private val getDevicesUseCase: GetDevicesUseCase,
    private val getLastIndoorMeasurementsUseCase: GetLastIndoorDevicesUseCase,
    private val registerUseCase: RegisterUseCase,
    private val changeUserDataUseCase: ChangeUserDataUseCase,
    private val loginUseCase: LoginUseCase,
    private val addDeviceUseCase: AddDeviceUseCase,
    private val getDevicesForUserUseCase: GetDevicesForUserUseCase,
    private val repo: AirQualityRepository
) : ViewModel() {

    var currentLocation: Location? = null

    var time = 0

    var job: Job = Job()

    var deviceId: Int = 0

    private var allDevices = mutableListOf<MarkerData>()

    private val _markers = MutableLiveData<List<Any>>()
    val markers: LiveData<List<Any>> = _markers

    private val _markersReady = MutableLiveData<Boolean>()
    val markersReady: LiveData<Boolean> = _markersReady

    private val _measurements = MutableLiveData<List<Measurements>>()
    val measurements: LiveData<List<Measurements>> = _measurements

    private val _allDevicesReady = MutableLiveData<Boolean>()
    val allDevicesReady: LiveData<Boolean> = _allDevicesReady

    private val _allMarkers = MutableLiveData<List<Any>>()
    val allMarkers: LiveData<List<Any>> = _allMarkers

    private val _personalDevices = MutableLiveData<List<Any>>()
    val personalDevices: LiveData<List<Any>> = _personalDevices

    private val _currentLocationAvailable = MutableLiveData<Unit>()
    val currentLocationAvailable: LiveData<Unit> = _currentLocationAvailable

    private val _devicesList = MutableLiveData<List<Int>>()
    val devicesList: LiveData<List<Int>> = _devicesList

    private val finalMarkerList = mutableListOf<MarkerData>()

    private var isAuthorized: Boolean? = null

    /**
     * Val/Var-s and LiveData for Auth/Login
     */

    private var userId: String = ""
    var name: String = ""
    var surname: String = ""
    var email: String = ""
    var phone: String = ""
    var organization: String = ""
    var passwd: String = ""


    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean> = _isLogged

    private val _showToast = MutableLiveData<EventWrapper<String>>()
    val showToast: LiveData<EventWrapper<String>> = _showToast

    private val _closeLogin = MutableLiveData<Unit>()
    val closeLogin: LiveData<Unit> = _closeLogin

    /**
     * END
     */

    /**
     *  Method used for getting outdoor devices data
     */

    fun getDataWithCoordinates() {
        var markersData: List<MarkerData>
        try {
            getLastOutdoorDevicesUseCase.execute {
                finalMarkerList.clear()

                markersData = it

                markersData.forEach { markerData ->
                    addDistanceToMarkerData(markerData)
                }

                _markersReady.value = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     *  Method used for calculating distance from the user location
     */
    private fun addDistanceToMarkerData(markerData: MarkerData) {
        val location = Location("Device")
        markerData.coordinates?.let { latlng ->
            location.latitude = latlng.latitude
            location.longitude = latlng.longitude
        }

        val distance = currentLocation?.distanceTo(location) ?: 1000000.0

        finalMarkerList.add(markerData.copy(distance = distance.toDouble().round(2)))
    }

    /**
     *  Method used for submitting all of the devices that contain location data
     */

    fun submitMarkers() {
        if(finalMarkerList.isNotEmpty()) {
        _markers.value = finalMarkerList
            .sortedBy { it.distance.toInt() }
            .filter { it.distance.toInt() < 3000 }
            .take(3)
        }
        else {
            _markers.value = listOf(UIEmptyItem())
        }
    }

    /**
     *  Method used for submitting all of the devices into single list (indoor + outdoor)
     */

    fun submitAllDevices() {
        if (allDevices.isNotEmpty()) {
            _allMarkers.value = allDevices
        }
        else
        {
            _allMarkers.value = listOf(UIEmptyItem())
        }
    }

    /**
     *  Method used for checking if the current location is available
     */

    fun checkCurrentLocation() {
        if (currentLocation != null) {
            _currentLocationAvailable.value = Unit
        }
    }

    /**
     *  Method used for getting all of the indoor and outdoor devices data
     */

    fun getAllDevicesData() {
        this.allDevices.clear()
        var allDevices = mutableListOf<MarkerData>()
        try {
            getLastIndoorMeasurementsUseCase.execute {
                it.forEach { measurement ->
                    allDevices.add(
                        MarkerData(
                            measurement
                        )
                    )
                }

                allDevices = allDevices.sortedByDescending { it.measurements.time }.toMutableList()
                allDevices.addAll(finalMarkerList)

                this.allDevices = allDevices

                _allDevicesReady.value = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     *  Method used for getting all of the indoor devices data
     */

    fun getDevices() {
        try {
            getDevicesUseCase.execute {
                _devicesList.value = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  Method used for filtering/finding needed device
     */

    fun doSearch(query: String = "") {
        viewModelScope.launch {
            val overallDevices: List<MarkerData> =
                allDevices.distinctBy { it.measurements.deviceId }

            val filteredList: List<MarkerData> = if (query.isNotEmpty() || query.isNotBlank()) {
                overallDevices.filter { it.measurements.deviceId.toString().contains(query) }
            } else {
                allDevices.distinctBy { it.measurements.deviceId }
            }
            if(filteredList.isNotEmpty()) {
            _allMarkers.value = filteredList
            }
            else {
                _allMarkers.value = listOf(UIEmptyItem())
            }

        }
    }

    private val timer = object : CountDownTimer(100000, 5000) {
        override fun onTick(millisUntilFinished: Long) {
            try {
                job = viewModelScope.launch(Dispatchers.IO) {
                    _measurements.postValue(repo.getMeasurementsByIdNotFlow(RequestBody(deviceId = deviceId)))
                    Log.d("Timer", "requests made: $time")
                    time += 1
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onFinish() {
            Log.d("Timer", "requests completed: $time")
            time = 0
        }
    }

    /**
     *  Method used for getting data of certain device by its ID
     */

    fun getMeasurementByDeviceId() {
        timer.cancel()
        time = 0
        job.cancel()
        timer.start()
    }

    fun resetTimer() {
        timer.cancel()
    }

    /**
     * Logics For Auth/Login
     */


    /**
     *  Method used for checking if the user is authorized
     */

    fun checkAuth() {
        isAuthorized?.let {
            _isLogged.value = it
        }
    }

    /**
     *  Method used for registering the user
     */

    fun register() {
        try {
            registerUseCase.execute(
                LoginBody(
                    name = name,
                    surname = surname,
                    email = email,
                    phone = phone,
                    password = passwd,
                    org = organization
                )
            ) {
                _showToast.value = EventWrapper(it)
                _closeLogin.value = Unit
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  Method handling the login by the user
     */

    fun login(email: String, password: String) {
        try {
            loginUseCase.execute(Pair(email, password)) {
                if (it.toIntOrNull() != null) {
                    userId = it
                    isAuthorized = true
                    _showToast.value = EventWrapper("Successful login!")
                    _closeLogin.value = Unit
                    getPersonalDevices()
                } else {
                    _showToast.value = EventWrapper(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  Method used for changing user data
     */

    fun changeCreds() {
        try {
            changeUserDataUseCase.execute(
                LoginBody(
                    id = userId,
                    name = name,
                    surname = surname,
                    email = email,
                    phone = phone,
                    password = passwd,
                    org = organization
                )
            ) {
                _showToast.value = EventWrapper(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  Method used for adding the device
     */

    fun addDeviceToUser() {
        try {
            if (userId.isNotEmpty()) {
                addDeviceUseCase.execute(Pair(userId, deviceId.toString())) {
                    _showToast.value = EventWrapper(it)
                }
            }
            getPersonalDevices()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  Method used getting personal devices list
     */

     fun getPersonalDevices() {
        val personalDevices = mutableListOf<Any>()
        getDevicesForUserUseCase.execute(userId.toInt()) {
            if(it.isNotEmpty()) {
                personalDevices.addAll(it)
            } else {
                personalDevices.add(UIEmptyItem())
            }
            _personalDevices.value = personalDevices
        }

    }
}