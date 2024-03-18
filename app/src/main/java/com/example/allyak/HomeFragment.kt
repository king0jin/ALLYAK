package com.example.allyak

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import com.prolificinteractive.materialcalendarview.CalendarDay
class HomeFragment : Fragment() {
    lateinit var searchSymptom: CardView
    lateinit var searchName: CardView
    private lateinit var TodayRecyclerView: RecyclerView
    private val pillList = ArrayList<PillListInfo>()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchSymptom = view.findViewById(R.id.searchSymptom)
        searchName = view.findViewById(R.id.searchName)
        searchSymptom.setOnClickListener {
            loadActivity(SearchsymptomActivity())
        }
        searchName.setOnClickListener {
            loadActivity(SearchnameActivity())
        }
        TodayRecyclerView = view.findViewById(R.id.todayRecyclerView)
        TodayRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        TodayRecyclerView.adapter = TodayAdapter(requireContext(), pillList)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("##INFO", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                userId = user.id.toString()
                getPillData(userId)
            }
        }
    }
    private fun getPillData(userId : String){
        val todayListener = object : ValueEventListener { //realtime database 가져오기
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                pillList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(PillListInfo::class.java)
                    // 리스트에 읽어 온 데이터를 넣어준다.
                    pillList.add(item!!)
                }
                (TodayRecyclerView.adapter as TodayAdapter).notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Allyakk", "loadPost:onCancelled", error.toException())
            }
        }
        MyRef.alarmRef.child(userId).child("${CalendarDay.today().year}${CalendarDay.today().month}${CalendarDay.today().day}").addValueEventListener(todayListener)
    }
    private fun loadActivity(activity: Activity) {
        val intent = Intent(this.requireContext(), activity::class.java)
        startActivity(intent)
    }
}