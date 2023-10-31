package com.masar.masatapp1.UI

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.masar.masatapp1.R
import java.io.IOException
import java.util.Locale

class CurrentLocation() : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.current_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        var latitude = intent.getDoubleExtra("latitude", 0.0)
        var longitude = intent.getDoubleExtra("longitude", 0.0)
        addMarker(latitude, longitude)
    }

    private fun addMarker(latitude: Double, longitude: Double) {
        val location = LatLng(latitude, longitude)

        // Remove any existing marker before adding a new one
        marker?.remove()
        val placeName = reverseGeocode(latitude,longitude)

        // Add a custom marker
        marker = googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(placeName)
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))
        marker?.showInfoWindow()
    }

    private fun reverseGeocode(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<android.location.Address>?

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (ioException: IOException) {
            // Network or other IO issues
            return "Geocoding failed: ${ioException.message}"
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Invalid latitude or longitude values
            return "Invalid latitude or longitude"
        }

        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            // Here, you can format the address parts as needed.
            val addressName = address.getAddressLine(0) ?: "Unknown Place"
            return addressName
        } else {
            return "No address found"
        }
    }

}
