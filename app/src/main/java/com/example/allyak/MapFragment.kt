package com.example.allyak

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

private val LOCATION_PERMISSION_REQUEST_CODE : Int = 1000

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var naverMap: NaverMap
    lateinit var mapView: MapView
    lateinit var locationSource: FusedLocationSource
    private lateinit var pharmacyViewModel: PharmacyLocationTaskViewModel
    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_map, container, false)
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        //map이 보입니다.
        return view

    }
    override fun onViewCreated(view: View, davedInstanceState: Bundle?) {
        super.onViewCreated(view, davedInstanceState)
        //pharmacyViewModel = ViewModelProvider(this).get(PharmacyLocationTaskViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as MapView?
        if (mapFragment != null) {
            //val newMapFragmenrt = MapFragment.newInstance()
                //childFragmentManager.beginTransaction().add(R.id.map_view, newMapFragmenrt).commit()
        }
        mapFragment?.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 사용자의 현재 위치를 가져와서 지도 중심으로 설정
        enableMyLocation()
        // 약국 위치를 가져와서 지도에 표시하는 함수 호출
        //loadNearbyPharmacies()


    }

//    private fun loadNearbyPharmacies() {
//        파싱한 약국 위치 지도에 표시할 예정
//
//    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            //권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return

        }
        // 현재 위치 버튼 활성화
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 위치 소스 설정
        naverMap.locationSource = locationSource
    }


}

