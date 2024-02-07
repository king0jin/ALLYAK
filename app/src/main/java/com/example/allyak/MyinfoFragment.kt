package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient

class MyinfoFragment : Fragment() {
    lateinit var addInfo: FloatingActionButton
    lateinit var auth: FirebaseAuth
//    lateinit var adapter: InfoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Firebase 인증 객체 가져오기
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_myinfo, container, false)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_action -> {
                //카카오 로그아웃
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.d("카카오","카카오 로그아웃 실패")
                    }else {
                        Log.d("카카오","카카오 로그아웃 성공!")
                        auth.signOut()
                        val intent = Intent(this.requireContext(), kakaoLogin::class.java)
                        startActivity(intent)
                    }
                }
                return true
            }
            else -> return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addInfo = view.findViewById(R.id.addInfo)
        addInfo.setOnClickListener {
            val intent = Intent(this.requireContext(), AddmyinfoActivity::class.java)
            startActivity(intent)
        }
    }
}