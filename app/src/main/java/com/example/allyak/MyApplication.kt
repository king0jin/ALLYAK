package com.example.allyak

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.naver.maps.map.NaverMapSdk

class MyApplication :MultiDexApplication(){
    companion object {
        lateinit var db: FirebaseFirestore
    }

    override fun onCreate() {
        super.onCreate()
        //firebase 데이터베이스
        db = FirebaseFirestore.getInstance()
        FirebaseApp.initializeApp(this)
        //  Kakao Sdk 초기화
        KakaoSdk.init(this, "a57851993143c72edcb786a7e4b45846")
        // MAPAPI를 호출해 클라이언트 ID를 지정
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("u3lhdtxvx3")
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Allyak", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("Allyak", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")
            }
        }
    }
}