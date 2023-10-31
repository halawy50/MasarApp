package com.masar.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.*
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.masar.masatapp1.UI.Student.stateData.SateData
import java.io.IOException
import java.util.Locale
import com.masar.masatapp1.R

import com.google.android.libraries.places.api.model.Place.Field

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
    GoogleMap.OnPoiClickListener {

    private lateinit var autoCompleteAdapter: AutocompleteAdapter
    private val locationPermissionCode = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var placesClient: PlacesClient
    private lateinit var selectAddress: Button
    private lateinit var current_location: Button

    private var addressNameMap = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        selectAddress = findViewById(R.id.selectAdress)
        current_location = findViewById(R.id.currentLocation)

        current_location.setOnClickListener {
            requestCurrentLocation()
        }

        selectAddress.setOnClickListener {
            if (addressNameMap.isNullOrEmpty() || latitude <= 0.0 || longitude <= 0.0) {
                showToast("حدد العنوان اولا")
            } else {
                SateData.address_map = addressNameMap
                SateData.latitude = latitude
                SateData.longitude = longitude // corrected assignment
//                showToast(SateData.address_map)
                onBackPressed()
            }
        }

        // Initialize the map
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        selectAddress = findViewById(R.id.selectAdress)
        if (!hasLocationPermission()) {
            requestLocationPermission()
        } else {
            requestCurrentLocation()
        }

        // Initialize the Places API
        Places.initialize(applicationContext, R.string.map_key.toString()) // Replace with your API key
        placesClient = Places.createClient(this)

        // Initialize the AutoCompleteTextView
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        autoCompleteAdapter = AutocompleteAdapter(this)
        autoCompleteTextView.setAdapter(autoCompleteAdapter)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedPlaceName = autoCompleteAdapter.getItem(position)
            if (selectedPlaceName != null) {
                searchForPlace(selectedPlaceName)
            }
        }

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                fetchAutocompletePredictions(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            locationPermissionCode
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val currentLocation = LatLng(it.latitude, it.longitude)
                        updateMarkerLocation(currentLocation)
                        animateCameraToLocation(currentLocation)
                    }
                }
        }
    }

    private fun updateMarkerLocation(location: LatLng) {
        marker?.position = location
        val placeName = reverseGeocode(location)
        addressNameMap = placeName
        latitude = location.latitude
        longitude = location.longitude // corrected assignment
    }

    private fun animateCameraToLocation(location: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(location))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.setOnMapLongClickListener(this)
        this.googleMap.setOnPoiClickListener(this)

        val initialLocation = LatLng(37.7749, -122.4194) // Example: San Francisco
        marker = googleMap.addMarker(MarkerOptions().position(initialLocation).title("Marker"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 13f))

//        // Add an InfoWindow to the marker
//        marker?.snippet = "This is the place information"
    }

    override fun onMapLongClick(latlng: LatLng) {
        updateMarkerLocation(latlng)
        val placeName = reverseGeocode(latlng)

        // Set the title and snippet for the InfoWindow
        marker?.title = placeName
        marker?.snippet = "Additional Information" // You can set this to any additional information you want to display

        // Show the InfoWindow
        marker?.showInfoWindow()
        searchForPlaceDetails(placeName)
        searchForPlace(placeName)

        addressNameMap = placeName.toString()
        latitude = latlng.latitude
        longitude = latlng.longitude
    }

    private fun searchForPlace(placeName: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setQuery(placeName)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                val predictions = response.autocompletePredictions
                if (predictions.isNotEmpty()) {
                    val firstPrediction = predictions[0]
                    val placeId = firstPrediction.placeId
                    val fields = listOf(Field.ID, Field.NAME, Field.LAT_LNG)
                    val detailsRequest = FetchPlaceRequest.newInstance(placeId, fields)

                    placesClient.fetchPlace(detailsRequest)
                        .addOnSuccessListener { placeResponse: FetchPlaceResponse ->
                            val place = placeResponse.place
                            val latLng = place.latLng

                            latLng?.let {
                                updateMarkerLocation(it)
                                animateCameraToLocation(it)
                            }

//                            showToast("Place Name: ${place.name}")
                        }
                        .addOnFailureListener { exception ->
                        }
                }
            }
            .addOnFailureListener { exception ->
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun fetchAutocompletePredictions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ADDRESS)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                val predictions = response.autocompletePredictions
                val placeNames = predictions.map { it.getPrimaryText(null).toString() }
                autoCompleteAdapter.updatePredictions(placeNames)
            }
            .addOnFailureListener { exception ->
//                showToast("Error fetching autocomplete predictions: ${exception.message}")
            }
    }

    private fun reverseGeocode(latlng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<android.location.Address>?

        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
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

    private fun searchForPlaceDetails(placeName: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setQuery(placeName)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                val predictions = response.autocompletePredictions
                if (predictions.isNotEmpty()) {
                    val firstPrediction = predictions[0]
                    val placeId = firstPrediction.placeId

                    // List of fields you want to retrieve from the place
                    val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

                    // Create a request to fetch place details using placeId and the desired fields
                    val detailsRequest = FetchPlaceRequest.newInstance(placeId, placeFields)

                    // Call the Places service to fetch place details
                    placesClient.fetchPlace(detailsRequest)
                        .addOnSuccessListener { placeResponse: FetchPlaceResponse ->
                            val place = placeResponse.place
                            val latLng = place.latLng
                            val address = place.address

                            latLng?.let {
                                updateMarkerLocation(it)
                                animateCameraToLocation(it)
                            }

                            // Display place information
                            val placeInfo = "Place Name: ${place.name}\nAddress: $address"
//                            showToast(placeInfo)
                        }
                        .addOnFailureListener { exception ->
//                            showToast("Error fetching place details: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
//                showToast("Error fetching autocomplete predictions: ${exception.message}")
            }
    }

    override fun onPoiClick(p0: PointOfInterest) {

        if (p0 != null) {
            val placeName = p0.name
            val latLng = p0.latLng

            // Update the marker's position and properties
            updateMarkerLocation(latLng)
            marker?.title = placeName
            marker?.snippet = "Additional Information" // You can set this to any additional information you want to display

            // Show the InfoWindow
            marker?.showInfoWindow()

//            // Display POI information using a Toast
//            val placeInfo =
//                "Place Name: $placeName\nLatitude: ${latLng.latitude}\nLongitude: ${latLng.longitude}"

            if (placeName.isNullOrEmpty() || latLng.latitude <= 0 || latLng.longitude <= 0) {
                showToast("حدد العنوان اولا")
            } else {
                addressNameMap = placeName.toString()
                latitude = latLng.latitude
                longitude = latLng.longitude
            }

            // You can also perform any other action you want here
        }
    }


}
