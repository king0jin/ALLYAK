package com.example.allyak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

class MapFragment : Fragment(){
    lateinit var naverMap: NaverMap
    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_map, container, false)
        val rootMapView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootMapView.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { naverMap ->
            this.naverMap = naverMap
            setupMap()


        }
        return rootMapView

    }

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
            .animate(com.naver.maps.map.CameraAnimation.Easing)

        naverMap.moveCamera(cameraUpdate)
        val marker = Marker()
        marker.position = initialPosition
        marker.map = naverMap

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        mapView = view.findViewById(R.id.map)
//
//    }


}

