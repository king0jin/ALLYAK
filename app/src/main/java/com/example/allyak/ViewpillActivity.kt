package com.example.allyak

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.allyak.databinding.ActivityViewpillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewpillActivity:AppCompatActivity() {
    private val binding by lazy{ ActivityViewpillBinding.inflate(layoutInflater)}
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                Glide.with(binding.root.context)
                    .load(intent.getStringExtra("image"))
                    .into(binding.pillImg)
                Log.d("Allyak", "bitmap")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Allyak", "error: $e")
            }
        }
        val itemNum = intent.getStringExtra("itemSeq") //품목 일련번호
        binding.pillName.text = intent.getStringExtra("itemName")
        binding.entpName.text = intent.getStringExtra("entpName")
        binding.efcyQesitm.text = intent.getStringExtra("efcyQesitm")
        binding.useMethod.text = intent.getStringExtra("useMethodQesitm")
        binding.useWarning.text = intent.getStringExtra("atpnQesitm")
        binding.depositMethod.text = intent.getStringExtra("deposit")
        binding.sideEffect.text = intent.getStringExtra("seQesitm")
        if(intent.getStringExtra("atpnWarnQesitm") == "null"){
            binding.warningPill.text = ""
        } else {
            binding.warningPill.text = intent.getStringExtra("atpnWarnQesitm")
        }
        //binding.warningPill.text = intent.getStringExtra("atpnWarnQesitm")
        binding.pillReviewBtn.setOnClickListener {
            val intent = Intent(this, AddreviewActivity::class.java)
            intent.putExtra("itemSeq", itemNum) //품목 일련번호
            startActivity(intent)
        }
        binding.mvPillinfo.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 터치 시작할 때 색상 변경
                    binding.mvPillinfo.setTextColor(Color.GRAY)
                    binding.scrollView.smoothScrollTo(0, binding.pillInfo.top)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 터치 종료 또는 취소될 때 색상 원래대로 변경
                    binding.mvPillinfo.setTextColor(Color.BLACK)
                }
            }
            true
        }
        binding.mvReview.setOnTouchListener{ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 터치 시작할 때 색상 변경
                    binding.mvReview.setTextColor(Color.GRAY)
                    binding.scrollView.smoothScrollTo(0, binding.pillReview.top)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 터치 종료 또는 취소될 때 색상 원래대로 변경
                    binding.mvReview.setTextColor(Color.BLACK)
                }
            }
            true
        }
    }
}
