package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class kakaoLogin : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    companion object {
        private const val TAG = "KakaoAuth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)
        //파이어베이스 인증 객체 초기화
        auth = FirebaseAuth.getInstance()
        //파이어베이스 함수 초기화
        functions = FirebaseFunctions.getInstance()

        val loginButton: ImageButton = findViewById(R.id.kakaobtn)

        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
            }
        }
        //카카오 로그인 성공시 호출되는 콜백
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(this, "카카오 계정으로 로그인 실패!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Toast.makeText(this, "카카오 계정으로 로그인 성공!", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                //firebase functions를 사용하여 카카오 토큰을 서버로 전송
                //CustomToken을 받아옴
                sendTokenToServer(token.accessToken)
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()

            }
        }
        //카카오 로그인 버튼 클릭
        loginButton.setOnClickListener {
            //카카오톡 실행가능 여부
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Toast.makeText(this, "카카오톡으로 로그인 실패!", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                    } else if (token != null) {
                        Toast.makeText(this, "카카오톡으로 로그인 성공!", Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        //firebase functions를 사용하여 카카오 토큰을 서버로 전송
                        //CustomToken을 받아옴
                        sendTokenToServer(token.accessToken)
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }
    private fun sendTokenToServer(kakakoAccessToken: String) {
        //firebase functions를 사용하여 카카오 토큰을 서버로 전송하고 CustonToken을 받아옴
        val data = hashMapOf(
            "accessToken" to kakakoAccessToken
        )
        functions.getHttpsCallable("createFirebaseUser")
            .call(data)
            .continueWith { task ->
                //firebasefunctions가 반환한 customtoken받음
                val customToken = task.result?.data as String
                Log.d("customToken", "custom Token received : $customToken")
                //firebase custom token을 사용하여 firebase에 로그인
                signInWithCustomToken(customToken)
            }

    }
    private fun signInWithCustomToken(customToken: String) {
        auth.signInWithCustomToken(customToken)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //로그인 성공
                    val user = auth.currentUser
                    Log.d("signInWithCustomToken", "signInWithCustomToken:success")
                    updateUI(user)
                } else {
                    //로그인 실패
                    Log.w("signInWithCustomToken", "signInWithCustomToken:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {
        //로그인 후 홈으로 이동
        if(user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, kakaoLogin::class.java)
            startActivity(intent)
        }
    }

}
