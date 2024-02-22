package com.example.allyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ActivitySearchsymptomBinding

class SearchsymptomActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchsymptomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchsymptom)
        val tb: Toolbar = findViewById(R.id.toolbar) //툴바 클릭 시 메인으로 이동
        tb.setOnClickListener{
            finish()
        }
        //binding.pillSearchRecyclerview.layoutManager = LinearLayoutManager(this)
       // binding.pillSearchRecyclerview.adapter = PillAdapter(this, itemList)

    }
}