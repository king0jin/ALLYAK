package com.example.allyak

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.kakao.sdk.common.util.Utility.getKeyHash
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Suppress("DEPRECATION")
class kakaoLogin : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)

        Log.d("getKeyHash", "" + getKeyHash(this))
    }
    fun getKeyHash(context: Context): String? {
        val pm: PackageManager = context.packageManager
        try {
            val packageInfo: PackageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                ?: return null
            for (signature: Signature in packageInfo.signatures) {
                try {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

}

class LoginScreen : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao Sdk 초기화
        com.kakao.sdk.common.KakaoSdk.init(this, "@string/kakao_app_key")
    }
}