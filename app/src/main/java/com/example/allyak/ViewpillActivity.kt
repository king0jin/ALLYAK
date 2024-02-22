package com.example.allyak

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.example.allyak.databinding.ActivityViewpillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewpillActivity:AppCompatActivity() {
    private val binding by lazy{ ActivityViewpillBinding.inflate(layoutInflater)}
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
        binding.pillName.text = intent.getStringExtra("itemName")
        binding.entpName.text = intent.getStringExtra("entpName")
        binding.efcyQesitm.text = intent.getStringExtra("efcyQesitm")
        binding.useMethod.text = intent.getStringExtra("useMethodQesitm")
        binding.useWarning.text = intent.getStringExtra("atpnQesitm")
        binding.depositMethod.text = intent.getStringExtra("deposit")
        binding.sideEffect.text = intent.getStringExtra("seQesitm")
        binding.testText.text = intent.getStringExtra("atpnWarnQesitm")
        //binding.pillReviewCnt.text =
        binding.pillReview.setOnClickListener {
            //다이얼로그 ? 아니면 새 액티비티 ?
        }
    }
}
