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
import com.graphhopper.GraphHopper

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MainActivity : AppCompatActivity()  {
    val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    public var i = 0

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
                    cal.locnoget("loction provided is not provided")
                }
            }
        } catch (e: SecurityException){
            e.printStackTrace()
        }
    }
    fun stealLocation() {
        steLocation( object : Locationcall {
            override fun locget(lat: Double, lon: Double) {
                // Use latitude and longitude here
                Log.d("Location", "Latitude: $lat, Longitude: $lon")
            }

            override fun locnoget(message: String) {
                // Handle error here
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
private lateinit var mapapi : Mapsapi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flc = LocationServices.getFusedLocationProviderClient(this)

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
                Toast.makeText(this, "location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}