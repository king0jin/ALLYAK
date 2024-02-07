package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CommunityFragment : Fragment() {
    // Access a Cloud Firestore instance from your Activity
    lateinit var addPost: FloatingActionButton
    private lateinit var communityRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_community, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPost = view.findViewById(R.id.addPost)
        addPost.setOnClickListener {
            val intent = Intent(this.requireContext(), AddpostActivity::class.java)
            startActivity(intent)
        }
        communityRecyclerView = view.findViewById(R.id.communityRecyclerView)
        //커뮤니티 데이터 가져오기
        MyApplication.db.collection("post")
            .get()
            .addOnSuccessListener {result ->
                val itemList = mutableListOf<ItemData>()
                for(document in result){
                    val item = document.toObject(ItemData::class.java)
                    item.docId=document.id
                    itemList.add(item)
                }
                communityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                //adapter = MyAdapter(requireContext(), itemList) copilot에서 자동완성으로 추가 밑코드는 내 코드
                communityRecyclerView.adapter = MyAdapter(requireContext(), itemList)
            /*  binding.mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.mainRecyclerView.adapter = MyAdapter(requireContext(), itemList)*/
            }
            .addOnFailureListener{exception ->
                Log.d("Allyak", "error.. getting document..", exception)
            }
    }
}