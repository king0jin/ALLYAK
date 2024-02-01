package com.example.allyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.allyak.databinding.ActivityMainBinding
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource



class MapFragmentActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1004

    private val PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: ActivityMainBinding
    private lateinit var naverMap : NaverMap
    private lateinit var locationSource : FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        //현재위치
        naverMap.locationSource = locationSource
        //현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
    }

}
