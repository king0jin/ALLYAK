package com.example.allyak

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class HomeFragment : Fragment() {
    lateinit var searchSymptom: CardView
    lateinit var searchName: CardView
    lateinit var searchShape: CardView
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
    }
    private fun loadActivity(activity: Activity) {
        val intent = Intent(this.requireContext(), activity::class.java)
        startActivity(intent)
    }
}