package com.primagiant.storyapp.features.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.primagiant.storyapp.R
import com.primagiant.storyapp.data.local.preference.SettingPreferenceViewModel
import com.primagiant.storyapp.data.remote.response.ListStoryItem
import com.primagiant.storyapp.databinding.ActivityMapsBinding
import com.primagiant.storyapp.utils.SettingViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var storyList: List<ListStoryItem>

    private val mapsViewModel: MapsViewModel by viewModels()
    private val settingPreferenceViewModel: SettingPreferenceViewModel by viewModels {
        SettingViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPreferenceViewModel.getToken().observe(this) { token ->
            mapsViewModel.getAllStory(token)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapsViewModel.listStory.observe(this) { story ->

            storyList = story

            if (story.isNullOrEmpty()) {
                Toast.makeText(this, "Belum ada data", Toast.LENGTH_SHORT).show()
            }
            mapFragment.getMapAsync(this)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        for (location in storyList) {

            if (location.lat != null && location.lon != null) {
                val latLngLocation = LatLng(location.lat, location.lon)

                mMap.addMarker(
                    MarkerOptions()
                        .position(latLngLocation)
                        .title(location.name)
                        .snippet(location.description)
                )

            }
        }

        val currentLocation = LatLng(0.7893, 113.9213)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 4f))

        getMyLocation()
        setMapStyle()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}