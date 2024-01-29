package com.example.allyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource



class MapFragmentActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var naverMap : NaverMap
    private lateinit var locationSource : FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 1004

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_map)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_view) as MapFragment?
            ?: MapFragment().also {
                fm.beginTransaction().add(R.id.map_view, it).commit()
            }
        //mapFragment.getMapAsync(this)
    }


    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true

        locationSource = FusedLocationSource(this@MapFragmentActivity, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }

    }
}
