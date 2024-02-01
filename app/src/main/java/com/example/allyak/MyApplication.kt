package com.example.allyak

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk

class MyApplication : Application(){


    override fun onCreate() {
        super.onCreate()
        //  Kakao Sdk 초기화
        KakaoSdk.init(this, "a57851993143c72edcb786a7e4b45846")
        // MAPAPI를 호출해 클라이언트 ID를 지정
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("u3lhdtxvx3")
    }
}