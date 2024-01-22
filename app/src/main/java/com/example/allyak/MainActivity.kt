package com.example.allyak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.allyak.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?){
        val binding = ActivityMainBinding.inflate(layoutInflater)
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
        // main에서 로그인 화면으로 자동 전환 코드
        val intent = Intent(this, kakaoLogin::class.java)
        startActivity(intent)
        finish()
        //bottomNav code
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.home)
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