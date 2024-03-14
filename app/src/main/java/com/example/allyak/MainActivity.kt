package com.example.allyak

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    private var mapFragment: MapFragment? = null // MapFragment 변수 추가
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
        //val key = "Cf%2FfmKfKPh4xVEzDeyvrjXkWpf3w%2BBEWgMkulFHU4JDbTxGMJYlzDH1QeKWI%2FAqtRIib8w02NBybR0vZXHgUPA%3D%3D"
        //val url = "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=$key&numOfRows=3&pageNo=1&type=json"
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Toolbar 로고 클릭 -> HomeFragment로 이동
        toolbar.setOnClickListener {
            bottomNav.setSelectedItemId(R.id.home)
            Log.d("Allyak", "Toolbar Clicked")
            true
        }
        //bottomNav code
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.home)
        if (intent.getStringExtra("destination") == "community") {
            bottomNav.setSelectedItemId(R.id.community)
            loadFragment(CommunityFragment())
        }
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.pill -> {
                    loadFragment(PillFragment())
                    true
                }

                R.id.map -> {
                    if (mapFragment == null) {
                        mapFragment = MapFragment()
                    }
                    loadFragment(mapFragment!!)
                    true
                }

                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.community -> {
                    loadFragment(CommunityFragment())
                    true
                }

                R.id.myInfo -> {
                    loadFragment(MyinfoFragment())
                    true
                }

                else -> {
                    Toast.makeText(this, "Navigation Error", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
}