package com.example.allyak

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import com.prolificinteractive.materialcalendarview.CalendarDay

class MyinfoFragment : Fragment() {
    private lateinit var userId: String
    lateinit var logout: TextView
    lateinit var addInfo: FloatingActionButton
    //복용중인 약 리스트
    private  lateinit var mediRecyclerView : RecyclerView
    private val mediItemList = mutableListOf<MyInfo>()
    //부작용 약 리스트
    private lateinit var sideRecyclerView : RecyclerView
    private val sideItemList = mutableListOf<MyInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myinfo, container, false)
        logout = view.findViewById(R.id.logoutBtn)
        logout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.d("카카오", "카카오 로그아웃 실패")
                } else {
                    Log.d("카카오", "카카오 로그아웃 성공!")
                    val intent = Intent(this.requireContext(), kakaoLogin::class.java)
                    startActivity(intent)
                }
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addInfo = view.findViewById(R.id.addInfo)
        addInfo.setOnClickListener {
            val intent = Intent(this.requireContext(), AddmyinfoActivity::class.java)
            startActivity(intent)
        }
    //현재 복용약
        mediRecyclerView = view.findViewById(R.id.mediRecyclerView)
        mediRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    //부작용
        sideRecyclerView = view.findViewById(R.id.sideRecyclerView)
        sideRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        UserApiClient.instance.me { user, error ->
            userId = user?.id.toString()
            getMediInfo(userId)
            getSideInfo(userId)
        } }
    private fun getMediInfo(uid : String){
        val infoListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mediItemList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(MyInfo::class.java)
                    if(item!!.medinow){
                        mediItemList.add(item)
                    }
                }
                (mediRecyclerView.adapter as InfoAdapter).notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Allyakk", "loadPost:onCancelled", error.toException())
            }
        }
        MyRef.infoRef.child(uid).addValueEventListener(infoListener)
        mediRecyclerView.adapter = InfoAdapter(requireContext(), mediItemList, "medi")
    }
    private fun getSideInfo(uid : String){
        val infoListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                sideItemList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(MyInfo::class.java)
                    if(item!!.medinot){
                        sideItemList.add(item)
                    }
                }
                (sideRecyclerView.adapter as InfoAdapter).notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Allyakk", "loadPost:onCancelled", error.toException())
            }
        }
        MyRef.infoRef.child(uid).addValueEventListener(infoListener)
        sideRecyclerView.adapter = InfoAdapter(requireContext(), sideItemList, "side")
    }
}