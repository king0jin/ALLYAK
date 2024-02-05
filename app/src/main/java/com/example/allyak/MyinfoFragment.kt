package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyinfoFragment : Fragment() {
    lateinit var addInfo: FloatingActionButton
//    lateinit var adapter: InfoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_myinfo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addInfo = view.findViewById(R.id.myInfo)
        addInfo.setOnClickListener {
            val intent = Intent(this.requireContext(), AddmyinfoActivity::class.java)
            startActivity(intent)
        }
    }
}