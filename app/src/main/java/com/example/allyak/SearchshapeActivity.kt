package com.example.allyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SearchshapeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchshape)
        val tb: Toolbar = findViewById(R.id.toolbar) //툴바 클릭 시 메인으로 이동
        tb.setOnClickListener{
            finish()
        }
    }
}