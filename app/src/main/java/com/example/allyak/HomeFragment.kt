package com.example.allyak

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
import com.kakao.sdk.user.UserApiClient

class HomeFragment : Fragment() {
    lateinit var searchSymptom: CardView
    lateinit var searchName: CardView
    lateinit var searchShape: CardView
    private lateinit var TodayRecyclerView: RecyclerView
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
        searchShape = view.findViewById(R.id.searchShape)
        searchSymptom.setOnClickListener {
            loadActivity(SearchsymptomActivity())
        }
        searchName.setOnClickListener {
            loadActivity(SearchnameActivity())
        }
        searchShape.setOnClickListener {
            loadActivity(SearchshapeActivity())
        }
        TodayRecyclerView = view.findViewById(R.id.todayRecyclerView)
        //약 정보 데이터 가져오기
        //이게 최선 ...?
        MyApplication.db.collection("pill")
            .get()
            .addOnSuccessListener {result ->
                val itemList = mutableListOf<TodayData>()
                for(document in result){
                    val item = document.toObject(TodayData::class.java)
                    item.docId=document.id
                    itemList.add(item)
                }
                //TodayRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                //TodayRecyclerView.adapter = TodayAdapter(requireContext(), itemList)
            }
            .addOnFailureListener{exception ->
                Log.d("Allyakk ", "error.. getting document..", exception)
            }
    }
    private fun loadActivity(activity: Activity) {
        val intent = Intent(this.requireContext(), activity::class.java)
        startActivity(intent)
    }
}