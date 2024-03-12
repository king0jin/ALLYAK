package com.example.allyak

//import android.widget.SearchView
import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import java.io.IOException
import java.util.Locale

private val LOCATION_PERMISSION_REQUEST_CODE : Int = 1000
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapFragment
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var searchView: SearchView

    private val pharmacyViewModel by lazy { PharmacyLocationTaskViewModel() }
    private val markers = mutableListOf<Marker>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        //mapView = childFragmentManager.findFragmentById(R.id.layout_map) as MapFragment
        //mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentLocation()
    }
    override fun onMapReady(naverMap: NaverMap) {
       this.naverMap = naverMap
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationSource = locationSource
        enableMyLocation()

        // 약국 데이터 변경 감지 및 처리
        pharmacyViewModel.pharmacyLocationData.observe(viewLifecycleOwner, Observer { pharmacyLocations ->

            // 약국 데이터를 사용하여 지도에 표시하는 코드 작성
            pharmacyLocations.forEach { pharmacyLocation ->
                Log.i("##INFO", "pharmacyLocation = ${pharmacyLocation.wgs84Lat} // ${pharmacyLocation.wgs84Lon}")
                // 약국 위치를 받아와서 지도에 마커로 표시하는 코드 작성
                val marker = Marker()
                marker.position = LatLng(pharmacyLocation.wgs84Lat.toDouble(), pharmacyLocation.wgs84Lon.toDouble())
                marker.map = naverMap
                marker.setOnClickListener {
                    val dlg = Dialog(requireContext(), R.style.theme_dialog)
                    dlg.setContentView(R.layout.bottomdialog)
                    val pharmacyName = dlg.findViewById<TextView>(R.id.tv_title)
                    val pharmacyAddress = dlg.findViewById<TextView>(R.id.tv_location)
                    val pharmacyTime = dlg.findViewById<TextView>(R.id.tv_time)
                    val pharmacyPhone = dlg.findViewById<TextView>(R.id.tv_phone)
                    val checkButton = dlg.findViewById<TextView>(R.id.bt_check)

                    checkButton.setOnClickListener {
                        dlg.dismiss()
                    }

                    pharmacyName.text = pharmacyLocation.dutyName
                    pharmacyAddress.text = "주소 : ${pharmacyLocation.dutyAddr}"
                    pharmacyTime.text = "운영시간 : ${pharmacyLocation.dutyTime1s} ~ ${pharmacyLocation.dutyTime1c}"
                    pharmacyPhone.text = "전화번호 : ${pharmacyLocation.dutyTel1}"
                    dlg.show()

                    false
                }
                markers.add(marker)
            }
        })

        val mLocationSource = FusedLocationSource(this, 100)
        naverMap.locationSource = mLocationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.5
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        // 위치 권한이 있을 경우 현재 위치 버튼 활성화
        naverMap.uiSettings.isLocationButtonEnabled = true
        // 현재 위치로 카메라 이동
        val location = locationSource.getLastLocation()
        val cameraUpdate = location?.let { LatLng(it.latitude, location.longitude) }
            ?.let { CameraUpdate.scrollTo(it) }
        if (cameraUpdate != null) {
            naverMap.moveCamera(cameraUpdate)
        }
    }
    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // 위치 좌표를 주소로 변환
                    getAddressFromLocation(location.latitude, location.longitude)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Log.e("Location", "Failed to get location: ${e.message}")
            }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale.KOREAN)
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0).substringAfter(" ")

                val cameraUpdate = CameraUpdate.scrollTo(LatLng(latitude, longitude))
                naverMap.moveCamera(cameraUpdate)

                val convertAddress = address.split(" ")
                pharmacyViewModel.getPharmacyLocationData(convertAddress[0].trim(), convertAddress[1].trim())

            } else {
                Log.e("Location", "No address found")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

