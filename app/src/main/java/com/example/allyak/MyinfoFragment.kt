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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class MyinfoFragment : Fragment() {
    lateinit var addInfo: FloatingActionButton
    //복용중인 약 리스트
    lateinit var medicineList : LinearLayout
    //부작용 약 리스트
    lateinit var sideEffectList : LinearLayout
    lateinit var logout: TextView
    //lateinit var auth: FirebaseAuth
//    lateinit var adapter: InfoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Firebase 인증 객체 가져오기
        //auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myinfo, container, false)
        medicineList = view.findViewById(R.id.medicineList)
        sideEffectList = view.findViewById(R.id.sideeffectList)
        logout = view.findViewById(R.id.logoutBtn)
        logout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.d("카카오", "카카오 로그아웃 실패")
                } else {
                    Log.d("카카오", "카카오 로그아웃 성공!")
//                  auth.signOut()
                    val intent = Intent(this.requireContext(), kakaoLogin::class.java)
                    startActivity(intent)
                }
            }
        }
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("myinfo")

        //데이터베이스에서 데이터 읽기
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //데이터를 가져와서 토글 상태에 따라 분류하여 각 리스트화면에 표시
                for (postSnapshot in dataSnapshot.children) {
                    val medinow = dataSnapshot.child("medinow").getValue(String::class.java)
                    val medinot = dataSnapshot.child("medinot").getValue(String::class.java)
                    val mediname = postSnapshot.child("mediname").getValue(String::class.java)
                    val memo = postSnapshot.child("memo").getValue(String::class.java)
                    if (medinow == "복용중") {
                        //medicineList에 약이름과 약메모 내용 표시
                        addTextViewToLinearLayout("$mediname - $memo\n", medicineList)

                    }
                    if (medinot == "부작용") {
                        //sideEffectList에 약이름과 약메모 내용 표시
                        addTextViewToLinearLayout("$mediname - $memo\n", sideEffectList)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 취소된 경우의 처리
                Log.e("Firebase", "onCancelled", databaseError.toException())
            }
        })
        return view

    }
    // LinearLayout에 TextView 추가하는 함수
    private fun addTextViewToLinearLayout(text: String, linearLayout: LinearLayout) {
        val textView = TextView(requireContext())
        textView.text = text
        linearLayout.addView(textView)
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