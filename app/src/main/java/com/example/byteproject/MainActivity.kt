package com.example.byteproject

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.preference.PreferenceManager
import com.graphhopper.GraphHopper
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MainActivity : AppCompatActivity()  {
    private lateinit var map: MapView
    private var loclist =  ArrayList<Pair<Double,Double>>()
    val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    public var i = 0


    private lateinit var mapapi : Mapsapi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flc = LocationServices.getFusedLocationProviderClient(this)
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContentView(R.layout.activity_main)
        map = findViewById(R.id.map)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            stealLocation()
        }

        val maphopper = GraphHopper()

        val lastlat : Double = 0.0
        val lastlon : Double = 0.0
        mapapi = Mapsapi(this, maphopper, lastlat, lastlon)
        mapapi.rout()
    }
    override fun onRequestPermissionsResult(
        requestcode: Int,
        permission: Array<out String>,
        grantResult: IntArray
    ) {
        super.onRequestPermissionsResult(requestcode, permission, grantResult)
        if (requestcode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResult.isNotEmpty() && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                stealLocation()
                i = 1
            } else {
                Toast.makeText(this, "location permission is denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    interface Locationcall{
        fun locget (lat: Double, lon: Double)
        fun locnoget (msg: String)
    }
    fun steLocation(cal: Locationcall) {
        val flc = LocationServices.getFusedLocationProviderClient(this)
        try {
            flc.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    cal.locget(lat, lon)
                } else {
                    val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
                        priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
                        interval = 1000
                        fastestInterval = 500
                    }
                    flc.requestLocationUpdates(locationRequest, object : com.google.android.gms.location.LocationCallback() {
                        override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                            locationResult.let {
                                val newLocation = it.lastLocation
                                if (newLocation != null) {
                                    cal.locget(newLocation.latitude, newLocation.longitude)
                                }
                            }
                        }
                    }, null)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    fun stealLocation() {
        steLocation( object : Locationcall {
            override fun locget(lat: Double, lon: Double) {
                Log.d("Location", "Latitude: $lat, Longitude: $lon")
                Toast.makeText(this@MainActivity, "Latitude: $lat, Longitude: $lon", Toast.LENGTH_LONG).show()
            }

            override fun locnoget(message: String) {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onStart() {
        super.onStart()
        map.onResume()
    }
    override fun onResume() {
        super.onResume()
        map.onResume()
    }
    override fun onPause() {
        super.onPause()
        map.onPause()
    }
    override fun onStop() {
        super.onStop()
        map.onDetach()
    }
    override fun onDestroy() {
        super.onDestroy()
        map.onDetach()
    }
}


