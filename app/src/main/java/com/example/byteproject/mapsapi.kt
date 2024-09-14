package com.example.byteproject

import android.Manifest
import android.location.Location
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import android.os.Looper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Callback
import java.io.IOException
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.graphhopper.GHRequest
import com.graphhopper.GHResponse
import com.graphhopper.GraphHopper

val mainkt = MainActivity()

class Mapsapi(private val context: Context, private val mapskey: GraphHopper,private val destinationLat: Double,
              private val destinationLon: Double) {
    private val apikey = "88e7e244-eb02-4c3b-abb7-b010b8342352"
    private val keyurl = "https://graphhopper.com/dashboard/#/apikeys"
    private val client = OkHttpClient()
    private lateinit var flc: FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    fun rout(){
        flc = LocationServices.getFusedLocationProviderClient(context)
        val locat = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 700
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
            flc.requestLocationUpdates(locat, Callback, Looper.getMainLooper())
    }
    private val Callback = object : LocationCallback() {
        fun locationresult(loc: LocationResult?){
            loc ?: return
            for(location in loc.locations){
                theGraphHopper(location)
            }
        }
    }
    private fun theGraphHopper(location: Location) {
        val ghRequest = GHRequest(location.latitude, location.longitude, destinationLat, destinationLon)
            .setProfile("walking")

        val response = mapskey.route(ghRequest)

        if (!response.hasErrors()) {
            val path = response.best
            val distance = path.distance
            val time = path.time

        } else {
            Log.e("GraphHopper", "Error: ${response.errors.joinToString()}")
        }
    }
}

