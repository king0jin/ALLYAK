package com.example.allyak

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class AuthCodeHandlerActivity : AppCompatActivity() {
    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("kakalogin", "로그인 실패 $error")
        } else if (token != null) {
            Log.e("kakaologin", "로그인 성공 ${token.accessToken}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            //카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                // 로그인 실패한다면?
                if (error != null) {
                    Log.e("kakaologin", "로그인 실패 $error")
                    //사용자가 취소한다면?
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    //그 외의 다른 오류가 발생한다면? => 카카오 이메일 로그인으로,,,?
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(
                            this,
                            callback = mCallback
                        )
                    }
                }
                // 로그인 성공!
                else if (token != null) {
                    Log.e("kakaologin", "로그인 성공 ${token.accessToken}")
                }
            }

        } else {
            //카카오 이메일 로그인
            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
        }
    }
}