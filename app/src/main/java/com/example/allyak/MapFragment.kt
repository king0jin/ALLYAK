package com.example.allyak

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource


class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var naverMap: NaverMap
    lateinit var mapView: MapView
    lateinit var pharmacyApiData: PharmacyApiData
    lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE : Int = 1000
    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_map, container, false)
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        searchView = view.findViewById(R.id.parmseach)
        //setHasOptionsMenu(true)아오 개빡치네.. 잘래
        //map이 보입니다.
        return view

    }
    override fun onViewCreated(view: View, davedInstanceState: Bundle?) {
        super.onViewCreated(view, davedInstanceState)
        mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        registerForContextMenu(mapView)

    }


    override fun onMapReady(naverMap: NaverMap) {
       // naverMap.setLocationSource(locationSource)
        this.naverMap = naverMap
        moveCameraToCurrentLocation()
        //사용자의 현재 위치를 가져와서 지도 중심으로 설정
        enableMyLocation()
        // 약국 위치를 가져와서 지도에 표시하는 함수 호출
        loadNearbyPharmacies()
        //마커리스너 설정
        naverMap.setOnMapClickListener { point, coord ->
            val marker = Marker()
            marker.position = LatLng(coord.latitude, coord.longitude)
            marker.map = naverMap
            val pharmacy = findPharmacyByMarker(marker)
            if (pharmacy != null) {
                showPharmacyInfoWindow(marker, pharmacy)
            }
        }
    }

    private fun moveCameraToCurrentLocation() {
        locationSource.lastLocation?.let {
            val latitude = it.latitude
            val longitude = it.longitude
            val cameraUpdate = com.naver.maps.map.CameraUpdate.scrollTo(LatLng(latitude, longitude))
            naverMap.moveCamera(cameraUpdate)
        }
    }

    //여기도 먼가 부족해.. 근데 너무 잠와..
    private fun enableMyLocation() {
        //위치 권한이 부여되어 있는지 확인
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
    private fun loadNearbyPharmacies() {
        // 현재 지도 화면의 영역을 구합니다.
        //

        //파싱한 약국 위치 지도에 표시할 예정
        pharmacyApiData = PharmacyApiData()
        val pharmacyList = pharmacyApiData.getData()
        for (pharmacy in pharmacyList) {
            val marker = Marker()
            marker.position = LatLng(pharmacy.wgs84Lat, pharmacy.wgs84Lon)
            marker.map = naverMap
        }
    }
    private fun findPharmacyByMarker(marker: Marker): Pharmacy? {
        val pharmacyList = pharmacyApiData.getData()
        return pharmacyList.find { it.wgs84Lat == marker.position.latitude && it.wgs84Lon == marker.position.longitude }
    }

    private fun showPharmacyInfoWindow(marker: Marker, pharmacy: Pharmacy) {
        marker.infoWindow?.close()

        val infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return "약국 이름: ${pharmacy.dutyName}\n주소: ${pharmacy.dutyAddr}\n전화번호: ${pharmacy.dutyTel1}" +
                        "\n진료시간\n 월요일: ${pharmacy.dutyTime1c} ~ ${pharmacy.dutyTime1s}\n" +
                        "화요일: ${pharmacy.dutyTime2c} ~ ${pharmacy.dutyTime2s}\n 수요일: ${pharmacy.dutyTime3c} ~ ${pharmacy.dutyTime3s}\n 목요일: ${pharmacy.dutyTime4c} ~ ${pharmacy.dutyTime4s}" +
                        "\n 금요일: ${pharmacy.dutyTime5c} ~ ${pharmacy.dutyTime5s}\n 토요일: ${pharmacy.dutyTime6c} ~ ${pharmacy.dutyTime6s}\n 일요일: ${pharmacy.dutyTime7c} ~ ${pharmacy.dutyTime7s}" +
                        "\n 공휴일: ${pharmacy.dutyTime8c} ~ ${pharmacy.dutyTime8s}"
            }
        }
        infoWindow.open(marker)
    }
}

