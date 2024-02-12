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
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
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
                    loadFragment(MapFragment())
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