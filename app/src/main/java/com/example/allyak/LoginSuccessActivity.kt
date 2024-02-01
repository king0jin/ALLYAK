package com.example.allyak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginSuccessActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success)

        //홈프래그먼트로 이동
        val intant = Intent(this, HomeFragment::class.java)
        intent.putExtra("fragment", "home")
        startActivity(intent)
        finish()
    }

}