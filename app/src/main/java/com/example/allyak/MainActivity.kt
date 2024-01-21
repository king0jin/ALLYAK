package com.example.allyak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // main에서 로그인화면으로 자동 전환 코드
        val intent = Intent(this, kakaoLogin::class.java)
        startActivity(intent)
        finish()

    }
}