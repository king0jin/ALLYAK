package com.example.allyak

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk

class LoginApplication : Application(){


    override fun onCreate() {
        super.onCreate()
        //  Kakao Sdk 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        // API를 호출해 클라이언트 ID를 지정
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("u3lhdtxvx3")
    }
}