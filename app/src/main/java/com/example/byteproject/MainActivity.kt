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

private const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {
    val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
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
            } else {
                Toast.makeText(this, "location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun stealLocation() {
        val flc = LocationServices.getFusedLocationProviderClient(this)

        try {
            flc.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    Log.d("Location", "Latitude: $lat, Longitude: $lon")
                    Toast.makeText(this, "Lat: $lat, Long: $lon", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "locatioon is null", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException){
            e.printStackTrace()
        }
    }
}