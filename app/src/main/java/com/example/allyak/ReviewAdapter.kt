package com.example.allyak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val items: List<String>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val symptomText: TextView = itemView.findViewById(R.id.symtag)
        val symptomCount: TextView = itemView.findViewById(R.id.symtagCnt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.symptomText.text = item
        holder.symptomCount.text = "0" // 초기값 설정
    }

    override fun getItemCount(): Int {
        return items.size
    }
}