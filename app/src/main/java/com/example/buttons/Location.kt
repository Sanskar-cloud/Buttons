package com.example.buttons

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Location : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val shareButton: Button = findViewById(R.id.share_button)
        shareButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted, so get the user's location and share it with others
                shareLocation()
            } else {
                // Permission is not granted, so request it from the user
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun shareLocation() {
        // Check whether the app has been granted the ACCESS_FINE_LOCATION permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted, so get the user's location and share it with others
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                // Get the latitude and longitude from the location object
                val latitude = location.latitude
                val longitude = location.longitude

                // Create a URI string for the user's current location
                val uriString  = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
                // Create an intent to share the location with others
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, uriString)
                }

                // Start the intent chooser
                startActivity(Intent.createChooser(shareIntent, "Share location"))
            }.addOnFailureListener { exception ->
                // Handle any exceptions that occur while getting the location
                Toast.makeText(
                    this,
                    "Failed to get location: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Permission is not granted, so request it from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}

