package com.example.allyak

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CommunityFragment : Fragment() {
    // Access a Cloud Firestore instance from your Activity
    //val db = Firebase.firestore
    lateinit var adapter: MyAdapter
    lateinit var addPost: FloatingActionButton

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
    }
    /*    private fun makeRecyclerView(){
            MyApplication.db.collection("post")
                .get()
                .addOnSuccessListener {result ->
                    val itemList = mutableListOf<ItemData>()
                    for(document in result){
                        val item = document.toObject(ItemData::class.java)
                        item.docId=document.id
                        itemList.add(item)
                    }
                    binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
                    binding.mainRecyclerView.adapter = MyAdapter(this, itemList)
                }
                .addOnFailureListener{exception ->
                    Log.d("Allyak", "error.. getting document..", exception)
                    Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }*/

}