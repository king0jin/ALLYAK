package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 2000
    companion object {
        private const val TAG = "KakaoAuth"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                    val intent = Intent(this, kakaoLogin::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user?.id}" +
                            "\n연결여부: ${user?.hasSignedUp}")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                //Splash 화면은 앱 처음 시작에 딱 1번만 보여지는 화면
                finish()
            }
        },SPLASH_DELAY)
    }

}