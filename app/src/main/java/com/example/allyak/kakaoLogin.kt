package com.example.allyak

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken


class kakaoLogin : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)
        
        //카카오 로그인
        //카카오계정으로 로그인 공통 callback 구성
        //카카오톡으로 로그인 할 수 없어 카카오톡계정으로 로그인할 경우 사용
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                //로그인 실패
                Log.e("kakaoLogin", "카카오계정으로 로그인 실패", error)
            }
            else if (token != null) {
                //로그인 성공
                Log.i("kakaoLogin", "카카오 계정으로 로그인 성공 ${token.accessToken}")
                //HomeFragment로 이동하는 코드


            }
        }

    }


}
