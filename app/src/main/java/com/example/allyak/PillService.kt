package com.example.allyak

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface PillService {
    @GET("1471000/DrbEasyDrugInfoService/getDrbEasyDrugList")
    fun getInfo( //가져오고자 하는 데이터
        @Query("serviceKey", encoded = true) serviceKey : String,
        @Query("pageNo") pageNo : String, //페이지 번호
        @Query("numOfRows") numOfRows : String, //한페이지 결과 수
        @Query("itemName") itemName : String,
        @Query("efcyQesitm") efcyQesitm : String, //증상
        @Query("type") type: String = "json"
    ): Call<PillInfo>
}
