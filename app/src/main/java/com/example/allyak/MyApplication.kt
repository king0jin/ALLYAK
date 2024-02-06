package com.example.allyak

import androidx.multidex.MultiDexApplication
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk

class MyApplication :MultiDexApplication(){  //원래 :Application() 이었음
//    companion object {
//        var instance : MyApplication? = null
//    }
//수경 코드
    companion object {
        //lateinit var auth: FirebaseAuth 인증 관리
        lateinit var db: FirebaseFirestore
/*        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }*/
    }

    override fun onCreate() {
        super.onCreate()
        //firebase 데이터베이스
        db = FirebaseFirestore.getInstance()
        //  Kakao Sdk 초기화
        KakaoSdk.init(this, "a57851993143c72edcb786a7e4b45846")
        // MAPAPI를 호출해 클라이언트 ID를 지정
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("u3lhdtxvx3")
    }
//    override fun onTerminate() {
//        super.onTerminate()
//        instance = null
//    }
//    fun getAppContext() : MyApplication {
//        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
//        return instance!!
//    }

}