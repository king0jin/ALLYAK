package com.example.allyak

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
            loadFragment(SearchSymptomFragment())
        }
        searchName.setOnClickListener {
            loadFragment(SearchNameFragment())
        }
        searchShape.setOnClickListener {
            loadFragment(SearchShapeFragment())
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout, fragment)
        transaction?.addToBackStack(null) //move to previous fragment
        transaction?.commit()
    }
}