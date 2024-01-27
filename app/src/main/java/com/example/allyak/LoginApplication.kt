package com.example.allyak

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk

class LoginApplication : Application(){

    companion object{
        var appContext : Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        KakaoSdk.init(this, getString(R.string.kakao_app_key))





    }

}