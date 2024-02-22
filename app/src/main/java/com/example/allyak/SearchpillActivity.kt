package com.example.allyak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ActivitySearchpillBinding
import com.example.allyak.databinding.ActivitySearchshapeBinding
//모양 검색 후 뜨는 액티비티
class SearchpillActivity: AppCompatActivity() {
    lateinit var binding: ActivitySearchpillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchpillBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        val recyclerView: RecyclerView = findViewById(R.id.pillSearchRecyclerview)
        
    }
}