package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient


class kakaoLogin : AppCompatActivity(){

    private var kakaoLoginButton: AppCompatImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)

        kakaoLoginButton = findViewById(R.id.kakaobtn)

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
                //HomeFragment로 이동하는 코드 - GoHome()으로 구현할 예정
                GoHome()
            }
        }
        //카카오 로그인
        kakaoLoginButton?.setOnClickListener() {
            //카카오 로그인 버튼이 클릭되었을 때 동작 구현
            if(UserApiClient.instance.isKakaoTalkLoginAvailable((this@kakaoLogin))) {
                UserApiClient.instance.loginWithKakaoTalk(this@kakaoLogin, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this@kakaoLogin, callback = callback)

            }

        }

    }

    fun GoHome(){
        //HomeFragment로 이동하는 코드
        val intent = Intent(this, HomeFragment::class.java)
        startActivity(intent)
        finish()
    }

}
