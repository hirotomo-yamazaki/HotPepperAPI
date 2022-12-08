package com.example.hotpepperapi

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.hotpepperapi.viewModel.ViewModel
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_REQUEST_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        location()
    }

    override fun onResume() {
        super.onResume()
        location()
    }

    @SuppressLint("MissingPermission")
    private fun location() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    viewModel.setLatLng(latitude, longitude)

                    Toast.makeText(this, "位置情報を利用しています。", Toast.LENGTH_LONG).show()
                } else {
                    val request = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(500)
                        .setFastestInterval(300)

                    fusedLocationClient
                        .requestLocationUpdates(
                            request,
                            object : LocationCallback() {
                                override fun onLocationResult(result: LocationResult) {
                                    val latitude = location?.latitude
                                    val longitude = location?.longitude
                                    viewModel.setLatLng(latitude, longitude)

                                    // 現在地だけ欲しいので、1回取得したらすぐに外す
                                    fusedLocationClient.removeLocationUpdates(this)
                                }
                            },
                            null
                        )
                }
                Log.i("success", viewModel.lat.value.toString())
            }
            .addOnFailureListener {
                    // 位置情報の権限が無いため、許可を求める
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        PERMISSION_REQUEST_CODE
                    )

                Log.i("failure", "fail")
            }
    }
}