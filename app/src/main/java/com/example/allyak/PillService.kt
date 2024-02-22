package com.example.allyak

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PillService {
    @GET("1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01")
    fun getInfo( //가져오고자 하는 데이터
        @Query("item_name") itemName: String, //품목명
        @Query("entp_name") entpName : String, //업체명
        @Query("item_seq") itemSeq : String, //품목일련번호
        @Query("img_regist_ts") imgRegistTs : String, //이미지 생성일
        @Query("pageNo") pageNo : String, //페이지 번호
        @Query("numOfRows") numOfRows : String, //한페이지 결과 수
        @Query("edi_code") ediCode : String,  //보험코드
        @Query("serviceKey") serviceKey : String,
        @Query("type") type: String = "json"
    ): Call<PillInfo>
}