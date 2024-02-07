package com.example.allyak

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ItemTodayBinding

class TodayViewHolder(val binding: ItemTodayBinding) : RecyclerView.ViewHolder(binding.root)
class TodayAdapter(val context: Context, val itemList: MutableList<TodayData>): RecyclerView.Adapter<TodayViewHolder>() {
    //데이터 구조를 이용해 보내야 되는건지 .. 알람이 요일을 고려하는지 ? 안했던거 같음
    //맞다면 시간 데이터와 약 이름 데이터를 리스트로 받기
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodayViewHolder(ItemTodayBinding.inflate(layoutInflater))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            todayPill.text = data.pill
            todayTime.text = data.time
        }
    }
}