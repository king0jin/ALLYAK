package com.example.allyak

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton


class kakaoLogin : AppCompatActivity(){

    private val kakaoAuthViewModle : KakaoAuthViewModle by viewModels()

    private var kakaoLoginButton: AppCompatImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)

        //카카오 로그인 버튼 클릭시, handleKakaoLogin() 함수 실행
        kakaoLoginButton = findViewById(R.id.kakaobtn)
        kakaoLoginButton?.setOnClickListener {
            kakaoAuthViewModle.kakaoLogin()
        }


    }


}
