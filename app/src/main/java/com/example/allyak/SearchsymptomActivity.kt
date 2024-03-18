package com.example.allyak

import com.example.allyak.databinding.ActivitySearchsymptomBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SearchsymptomActivity : AppCompatActivity() {
    private val binding by lazy{ ActivitySearchsymptomBinding.inflate(layoutInflater)}
    private val adapter by lazy {PillAdapter(dataList)}
    private val dataList = mutableListOf<Item?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.pillSearchRecyclerview.adapter = adapter
        binding.pillSearchRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.toolbar.setOnClickListener{
            finish()
        }
        binding.pillSearchBtn.setOnClickListener {
            val mySymptom = binding.searchSymptomText.text.toString()
            pillRequest(mySymptom)
        }
    }
    fun pillRequest(mySymptom: String) {
        //1.Retrofit 객체 초기화
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/")
            .addConverterFactory(ScalarsConverterFactory.create()) // 스칼라 변환기 추가
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        //2. 서비스 객체 생성
        val pillService:PillService = retrofit.create(PillService::class.java)
        //3. Call 객체 생성
        val apiKey = BuildConfig.MY_KEY
        val pillCall = pillService.getInfo(apiKey, "1","50","",mySymptom)
        if (dataList.isNotEmpty()) {
            dataList.clear()
        }
        Log.d("API_Request", "URL: ${pillCall.request().url}")
        pillCall.enqueue(object : Callback<PillInfo>{
            override fun onResponse(call: Call<PillInfo>, response: Response<PillInfo>) {
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
            }
            override fun onFailure(call: Call<PillInfo>, t: Throwable) {
                Log.e("API_Request_Failure", "Error: ${t.message}", t)
            }
        })
    }
}