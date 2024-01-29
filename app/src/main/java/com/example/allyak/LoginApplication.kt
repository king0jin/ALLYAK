package com.example.allyak

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class LoginApplication : Application(){

//  Kakao Sdk 초기화
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}