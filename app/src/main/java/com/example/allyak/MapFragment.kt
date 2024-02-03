package com.example.allyak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.overlay.Marker

//private val LOCATION_PERMISSION_REQUEST_CODE : Int = 1000
class MapFragment : Fragment(){
    lateinit var naverMap: NaverMap
    lateinit var mapView: MapView
//    lateinit var locationSource: FusedLocationSource
//    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        NaverMapSdk.getInstance(requireContext()).client = NaverMapSdk.NaverCloudPlatformClient("u3lhdtxvx3")
        //return inflater.inflate(R.layout.fragment_map, container, false)
        val rootMapView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootMapView.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)
        //locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        mapView.getMapAsync { naverMap ->
            this.naverMap = naverMap
            setupMap()
        }
//        requestPermissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()) { isGrantes ->
//            if(isGrantes) {
//                setupMap()
//            } else {
//                Toast.makeText(requireContext(), "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
//            }
//        }
        return rootMapView

    }

//    override fun onMapReady(naverMap: NaverMap) {
//        this.naverMap = naverMap
////        naverMap.locationSource = locationSource
//        naverMap.locationTrackingMode = LocationTrackingMode.Follow
//        setupMap()
//    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    private fun setupMap() {
        val initialPosition = LatLng(37.5666103, 126.9783882)
        val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
            .animate(CameraAnimation.Easing)

        naverMap.moveCamera(cameraUpdate)

        val marker = Marker()
        marker.position = initialPosition
        marker.map = naverMap
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            naverMap.locationTrackingMode = LocationTrackingMode.Follow
//            naverMap.locationSource = locationSource
//            naverMap.uiSettings.isLocationButtonEnabled = true
//            naverMap.uiSettings.isCompassEnabled = true
//            val lastLocation = locationSource.lastLocation
//            lastLocation?.let {
//                val initialPosition = LatLng(it.latitude, it.longitude)
//                val cameraUpdate = CameraUpdate.scrollTo(initialPosition)
//                    .animate(CameraAnimation.Easing)
//                naverMap.moveCamera(cameraUpdate)
//                val marker = Marker()
//                marker.position = initialPosition
//                marker.map = naverMap
//            } ?: run {
//                Toast.makeText(requireContext(), "현재 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
//            }
//        } else {
////            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            return
//
//        }

//        naverMap.locationTrackingMode = LocationTrackingMode.Follow
//        naverMap.locationSource = locationSource
//        val lastLocation = locationSource.lastLocation
//        lastLocation?.let {
//            val initalPosition = LatLng(it.latitude, it.longitude)
//            val cameraUpdate = CameraUpdate.scrollTo(initalPosition)
//                .animate(com.naver.maps.map.CameraAnimation.Easing)
//            naverMap.moveCamera(cameraUpdate)
//            val marker = Marker()
//            marker.position = initalPosition
//            marker.map = naverMap
//        } ?: run {
//            Toast.makeText(requireContext(), "현재 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
//
//        }



    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            LOCATION_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    setupMap()
//                } else {
//                    Toast.makeText(requireContext(), "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT)
//                        .show()
//
//                }
//
//            }
//        }
//    }

//    override fun onLocationChanged(location: Location?) {
//        location?.let {
//            val newPosition = LatLng(it.latitude, it.longitude)
//            val cameraUpdate = CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude))
//                .animate(CameraAnimation.Easing)
//            this.naverMap.moveCamera(cameraUpdate)
//        }
//    }


}

