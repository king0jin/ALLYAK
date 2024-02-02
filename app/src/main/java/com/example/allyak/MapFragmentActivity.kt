package com.example.allyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


abstract class MapFragmentActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        val fm = supportFragmentManager
//        val mapFragment = fm.findFragmentById(R.id.map_view) as MapFragment?
//            ?: MapFragment.newInstance().also {
//                fm.beginTransaction().add(R.id.map_view, it).commit()
//            }
//        mapFragment.getMapAsync(this)

    }




}

