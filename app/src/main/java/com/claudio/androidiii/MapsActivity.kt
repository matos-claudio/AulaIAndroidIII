package com.claudio.androidiii

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.claudio.androidiii.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    val start = LatLng(-15.7938, -47.882) // Nova York
    val end = LatLng(-15.7968, -47.8926) // Los Angeles

    private lateinit var googleMap: GoogleMap
    private lateinit var handler: Handler
    private val startPosition = LatLng(start.latitude, start.longitude)
    private val endPosition = LatLng(end.latitude, end.longitude)
    private val duration = 10000L // 10 segundos
    private val updateInterval = 3000L // 1 segundo
    private var startTime: Long = 0

    private val runnable = object : Runnable {
        override fun run() {
            val elapsedTime = System.currentTimeMillis() - startTime
            val fraction = elapsedTime.toFloat() / duration
            val newPosition = LatLng(
                startPosition.latitude + (endPosition.latitude - startPosition.latitude) * fraction,
                startPosition.longitude + (endPosition.longitude - startPosition.longitude) * fraction
            )
            googleMap = mMap
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(newPosition).title("Moving Marker"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition))

            if (elapsedTime < duration) {
                handler.postDelayed(this, updateInterval)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        handler = Handler(Looper.getMainLooper())

//        val distance = distanceBetweenTwoPoints(start, end)
        val distance = distanceBetweenTwoPoints(start, end) / 1000
//        println("Distância entre Nova York e Los Angeles: $distance metros")
        val formattedDistance = "%.2f".format(distance) // Arredondar para duas casas decimais
        println("Distância entre Nova York e Los Angeles: $formattedDistance km")
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-15.7968, -47.8926)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        mMap.maxZoomLevel

//        googleMap = map

        val zoomLevel = 15f // Ajuste o nível de zoom conforme necessário (1 é o zoom máximo)
        val cameraPosition = CameraPosition.Builder()
            .target(startPosition)
            .zoom(zoomLevel)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        startTime = System.currentTimeMillis()
//        handler.postDelayed(runnable, updateInterval)
    }

    fun distanceBetweenTwoPoints(start: LatLng, end: LatLng): Double {
        val lat1 = start.latitude
        val lon1 = start.longitude
        val lat2 = end.latitude
        val lon2 = end.longitude

        val R = 6371 // raio da Terra em quilômetros
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = R * c * 1000 // converter para metros

        return distance
    }
}