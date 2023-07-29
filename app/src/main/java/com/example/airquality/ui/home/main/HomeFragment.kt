package com.example.airquality.ui.home.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.example.airquality.R
import com.example.airquality.databinding.FragmentHomeBinding
import com.example.airquality.utils.observe
import com.google.android.gms.location.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

/**
 * HomeFragment Class, responsible for showing devices nearby, all devices and personal devices
 */
@Suppress("DEPRECATION")
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val homeViewModel: HomeViewModel by sharedViewModel()

    private val multiTypeAdapter = MultiTypeAdapter()

    private val allMultiTypeAdapter = MultiTypeAdapter()

    private val personalMultiTypeAdapter = MultiTypeAdapter()

    private var fusedLocationClient: FusedLocationProviderClient? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission {
            if (it) {
                getLocation()
            }
        }
        setupView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.checkAuth()
    }

    private fun setupView() = with(binding) {
        homeViewModel.getDevices()
        homeHeaderLayout.settingsImageView.setOnClickListener {
            findNavController().navigate(R.id.home_to_settings)
        }
        homeHeaderLayout.loginImageView.setOnClickListener {
            findNavController().navigate(R.id.home_to_login)
        }
        multiTypeAdapter.apply {
            register(
                DeviceInfoCardViewBinder {
                    homeViewModel.deviceId = it.measurements.deviceId
                    it.coordinates?.let { latlng ->
                        val location = getLocationName(latlng.latitude, latlng.longitude)
                        val dialog = DetailedDeviceInfoDialog.newInstance(
                            it.measurements,
                            location
                        )
                        dialog.onDismiss = {
                            homeViewModel.resetTimer()
                        }
                        dialog.show(childFragmentManager, this.javaClass.name)
                    }
                }
            )
            register(EmptyGlobalViewBinder())
        }
        allMultiTypeAdapter.apply {
            register(
                DeviceInfoCardViewBinder {
                    homeViewModel.deviceId = it.measurements.deviceId
                    if (it.coordinates != null) {
                        val location =
                            getLocationName(it.coordinates.latitude, it.coordinates.longitude)
                        val dialog = DetailedDeviceInfoDialog.newInstance(
                            it.measurements,
                            location
                        )
                        dialog.onDismiss = {
                            homeViewModel.resetTimer()
                        }
                        dialog.show(childFragmentManager, this.javaClass.name)
                    } else {
                        homeViewModel.deviceId = it.measurements.deviceId
                        val location = root.context.getString(R.string.indoor)
                        val dialog = DetailedDeviceInfoDialog.newInstance(
                            it.measurements,
                            location
                        )
                        dialog.onDismiss = {
                            homeViewModel.resetTimer()
                        }
                        dialog.show(childFragmentManager, this.javaClass.name)
                    }
                }
            )
            register(EmptyGlobalViewBinder())
        }
        personalMultiTypeAdapter.apply {
            register(
                DeviceInfoCardViewBinder {
                    homeViewModel.deviceId = it.measurements.deviceId
                    if (it.coordinates != null) {
                        val location =
                            getLocationName(it.coordinates.latitude, it.coordinates.longitude)
                        val dialog = DetailedDeviceInfoDialog.newInstance(
                            it.measurements,
                            location
                        )
                        dialog.onDismiss = {
                            homeViewModel.resetTimer()
                        }
                        dialog.show(childFragmentManager, this.javaClass.name)
                    } else {
                        homeViewModel.deviceId = it.measurements.deviceId
                        val location = root.context.getString(R.string.indoor)
                        val dialog = DetailedDeviceInfoDialog.newInstance(
                            it.measurements,
                            location
                        )
                        dialog.onDismiss = {
                            homeViewModel.resetTimer()
                        }
                        dialog.show(childFragmentManager, this.javaClass.name)
                    }
                }
            )
            register(EmptyGlobalViewBinder())
        }

        yourDevsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = personalMultiTypeAdapter
        }
        nearestSensorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = multiTypeAdapter
        }
        allSensorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allMultiTypeAdapter
        }

        settingsImageView.setOnClickListener {
            findNavController().navigate(R.id.home_to_settings)
        }
        addDeviceImageView.setOnClickListener {
            val dialog = AddPersonalDeviceDialog()
            dialog.show(childFragmentManager, this.javaClass.name)
        }

        homeViewModel.getAllDevicesData()
    }

    private fun observeViewModel() = with(homeViewModel) {
        with(binding) {
            observe(isLogged, viewLifecycleOwner) {
                homeHeaderLayout.loginImageView.isVisible = !it
                homeHeaderLayout.settingsImageView.isVisible = !it
                personalAreaLayout.isVisible = it

            }
            observe(currentLocationAvailable, viewLifecycleOwner) {
                homeViewModel.getDataWithCoordinates()
            }
            observe(markersReady, viewLifecycleOwner) {
                homeViewModel.submitMarkers()
            }
            observe(allDevicesReady, viewLifecycleOwner) {
                homeViewModel.submitAllDevices()
            }
            observe(devicesList, viewLifecycleOwner) {
                val adapter = ArrayAdapter(
                    root.context,
                    android.R.layout.simple_dropdown_item_1line,
                    it
                )
                autoCompleteTextView.setAdapter(adapter)
                autoCompleteTextView.addTextChangedListener {
                    homeViewModel.doSearch(it.toString())
                }
            }

            observe(markers, viewLifecycleOwner) {
                multiTypeAdapter.items = it
                multiTypeAdapter.notifyDataSetChanged()
            }

            observe(allMarkers, viewLifecycleOwner) {
                allMultiTypeAdapter.items = it
                allMultiTypeAdapter.notifyDataSetChanged()
            }
            observe(personalDevices, viewLifecycleOwner) {
                println("Hello $it")
                personalMultiTypeAdapter.items = it
                personalMultiTypeAdapter.notifyDataSetChanged()
            }

        }
    }


    /**
     *  Method used for getting location name
     */

    private fun getLocationName(latitude: Double, longitude: Double): String {
        val gcd = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = gcd.getFromLocation(latitude, longitude, 1)
        return if (addresses.isNotEmpty()) {
            addresses[0].locality
        } else {
            ""
        }
    }

    /**
     *  Method used for getting location/coordinates
     */

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(binding.root.context)
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 2
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        homeViewModel.currentLocation = location
                        homeViewModel.checkCurrentLocation()
                    }
                }
            }
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    /**
     *  Method used for requesting permission for getting location/coordinates
     */

    private fun getLocationPermission(result: (Boolean) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                binding.root.context,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                binding.root.context,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            result.invoke(true)
        } else {
            result.invoke(false)
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                44
            )
        }
    }

}