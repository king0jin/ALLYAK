package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allyak.databinding.ActivitySearchnameBinding
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SearchnameActivity: AppCompatActivity() {
    private val binding by lazy{ ActivitySearchnameBinding.inflate(layoutInflater)}
    private val adapter by lazy {PillAdapter(dataList)}
    private val dataList = mutableListOf<Item?>()
    private var currentCall: Call<PillInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pillSearchRecyclerview.adapter = adapter
        binding.pillSearchRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.toolbar.setOnClickListener{
            finish()
        }
        binding.test.setOnClickListener {
            startActivity(Intent(this, ViewpillActivity::class.java))
        }
        binding.pillSearchBtn.setOnClickListener {
            if (currentCall == null) {
                val pillName = binding.searchNameText.text.toString()
                Toast.makeText(this, "$pillName 정보 불러오는 중", Toast.LENGTH_SHORT).show()
                pillRequest(pillName)
            }
        }
    }
    fun pillRequest(pillName: String) {
        //1.Retrofit 객체 초기화
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/")
            .addConverterFactory(ScalarsConverterFactory.create()) // 스칼라 변환기 추가
            .addConverterFactory(GsonConverterFactory.create(gson)) //gson 넣을지 말지
            .build()
        //2. 서비스 객체 생성
        val pillService:PillService = retrofit.create(PillService::class.java)
        //3. Call 객체 생성
        val apiKey = BuildConfig.MY_KEY
        val pillCall = pillService.getInfo(apiKey, "1","50",pillName,"")
        if (dataList.isNotEmpty()) {
            dataList.clear()
        }
        Log.d("API_Request", "URL: ${pillCall.request().url}")
        currentCall = pillCall
        pillCall.enqueue(object : Callback<PillInfo>{
            override fun onResponse(call: Call<PillInfo>, response: Response<PillInfo>) {
                currentCall = null
                val data = response.body()
                val pillList = data?.body?.items
                if (!pillList.isNullOrEmpty()) {
                    pillList.let { info ->
                        info.forEach {
                            dataList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                Log.d("API_Request_Success", "Success: $data")
            }
            override fun onFailure(call: Call<PillInfo>, t: Throwable) {
                currentCall = null
                Log.e("API_Request_Failure", "Error: ${t.message}", t)
            }
        })
    }
}