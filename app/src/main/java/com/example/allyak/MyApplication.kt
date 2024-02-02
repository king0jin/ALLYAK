package com.example.allyak

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk

class MyApplication : Application(){
    companion object {
        var instance : MyApplication? = null
    }


    override fun onCreate() {
        super.onCreate()


        //  Kakao Sdk 초기화
//        if(KakaoSdk.getAdapter == null){
//            KakaoSDK.init(KakaoSDKAdapter(getAppContext()))
//        }
        KakaoSdk.init(this, "a57851993143c72edcb786a7e4b45846")
        // MAPAPI를 호출해 클라이언트 ID를 지정

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("u3lhdtxvx3")
    }
    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
    fun getAppContext() : MyApplication {
        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
        return instance!!
    }

}