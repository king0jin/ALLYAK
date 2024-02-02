package com.example.allyak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginSuccessActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success)

        //홈프래그먼트로 이동
//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, HomeFragment::class.java)
//            startActivity(intent)
//            finish()
//        }, 2000)
    }

}