package com.example.allyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MapFragmentActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1004

    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }





}
