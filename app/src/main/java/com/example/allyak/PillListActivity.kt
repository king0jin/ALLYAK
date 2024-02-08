package com.example.allyak

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PillListActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pill_list)
        val alarmList = findViewById<TextView>(R.id.alarmList)
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val mediName = intent.getStringExtra("mediName")
        alarmList.text = "알람 시간 | $hour:$minute | 약 이름 $mediName"

        val addPill = findViewById<FloatingActionButton>(R.id.addPillButton)
        addPill.setOnClickListener {
            val intent = Intent(this, AddAlramActivity::class.java)
            startActivity(intent)
        }
    }
}