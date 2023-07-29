package com.example.airquality.ui.maps

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.airquality.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


/**
 * MapsFragment class responsible for map interaction
 */

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val KAZ_CRD = LatLng(48.934028, 67.202941)
    private val mapsViewModel by viewModel<MapsViewModel>()
    private var markerList: MutableList<Pair<Marker?, Int>> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapFragment = this.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapsViewModel.getMapMarkerData()
        return view
    }


    /**
     *  Method used for getting location name
     */

    private fun getLocationName(latitude:Double, longitude: Double): String {
        val gcd = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = gcd.getFromLocation(latitude, longitude, 1)
        return if (addresses.isNotEmpty()) {
            addresses[0].locality
        } else {
            ""
        }
    }

    /**
     *  Method used for creating markers, which depict device location
     */

    private fun createMarker(deviceLocation: LatLng, aqi: Int): MarkerOptions {
        val iconGen = IconGenerator(requireContext())
        iconGen.setBackground(
            ResourcesCompat.getDrawable(
                resources,
                identifyBackgroundColor(aqi),
                null
            )
        )
        iconGen.setTextAppearance(R.style.MarkerText)
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(aqi.toString())))
            .position(deviceLocation)
            .anchor(iconGen.anchorU, iconGen.anchorV)

        return markerOptions
    }

    /**
     *  Method used for drawing zones around markers
     */

    private fun setMeasurementZones(point: LatLng, color: Int) {
        val circleOptions = CircleOptions().apply {
            center(point)
            radius(150.0)
            fillColor(ContextCompat.getColor(requireContext(), color))
            strokeWidth(0.1f)
            strokeColor(ContextCompat.getColor(requireContext(), R.color.colorGray))
        }
        mMap.addCircle(circleOptions)
    }

    /**
     *  Method used for identifying background color of the marker, depending on AQI level
     */

    private fun identifyBackgroundColor(aqi: Int): Int {
        return when {
            aqi >= 151 -> R.drawable.marker_red
            aqi in 101..150 -> R.drawable.marker_orange
            aqi in 51..100 -> R.drawable.marker_yellow
            aqi in 31..50 -> R.drawable.marker_salad
            else -> R.drawable.marker_green
        }
    }

    /**
     *  Method used for identifying background color of the zone, depending on AQI level
     */


    private fun identifyCircleColor(aqi: Int): Int {
        return when {
            aqi >= 151 -> R.color.colorFillRedAlpha40
            aqi in 101..150 -> R.color.colorFillYellowAlpha40
            aqi in 51..100 -> R.color.colorFillOrangeAlpha40
            aqi in 31..50 -> R.color.colorFillSaladAlpha40
            else -> R.color.colorFillGreenAlpha40
        }
    }

    /**
     *  callback method is called when the map is ready to function
     */

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        observeViewModel()

        mMap.setOnMarkerClickListener { marker ->
            val selectedItem = markerList.find {
                it.first?.id.equals(marker.id)
            }
            val selectedData = mapsViewModel.refinedMarkerData.find {
                it.measurements.deviceId == selectedItem?.second
            }
            val location = selectedData?.coordinates?.let {
                 getLocationName(it.latitude, it.longitude)
            }

            if (selectedData != null) {
                MapParticleIndicationDialog.newInstance(
                    selectedData.measurements,
                    location = location!!
                ).also {
                    it.show(childFragmentManager, this.javaClass.name)
                }
            }
            true
        }
        checkMode()
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(KAZ_CRD, 4.0f)
        )
    }
    /**
     *  Method used for checking the theme (dark or light)
     */

    private fun checkMode() {
        when (requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                mMap.setMapStyle(
                    MapStyleOptions(resources.getString(R.string.night_style_json))
                )
            }
            Configuration.UI_MODE_NIGHT_NO -> {

                mMap.setMapStyle(
                    MapStyleOptions(resources.getString(R.string.day_style_json))
                )
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                mMap.setMapStyle(
                    MapStyleOptions(resources.getString(R.string.day_style_json))
                )
            }
        }
    }

    private fun observeViewModel() = with(mapsViewModel) {
        markers.observe(viewLifecycleOwner) {
            it.forEach { data ->
                markerList.add(
                    Pair(
                        mMap.addMarker(
                            createMarker(
                                data.coordinates!!,
                                data.measurements.quality.toInt()
                            )
                        ),
                        data.measurements.deviceId
                    )
                )
                setMeasurementZones(
                    data.coordinates,
                    identifyCircleColor(data.measurements.quality.toInt())
                )
            }
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    it.first().coordinates!!,
                    10.5f
                )
            )
        }
    }


}