package com.example.allyak

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ItemTodayBinding
import com.kakao.sdk.user.UserApiClient

class TodayViewHolder(val binding: ItemTodayBinding) : RecyclerView.ViewHolder(binding.root)
class TodayAdapter(val context: Context, val itemList: MutableList<PillListInfo>): RecyclerView.Adapter<TodayViewHolder>() {
    //데이터 구조를 이용해 보내야 되는건지 .. 알람이 요일을 고려하는지 ? 안했던거 같음
    //맞다면 시간 데이터와 약 이름 데이터를 리스트로 받기
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodayViewHolder(ItemTodayBinding.inflate(layoutInflater))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    // 약의 체크 여부를 업데이트하는 함수
    private fun updatePillCheckStatus(position: Int, userId: String, isChecked: Boolean) {
        val pill = itemList[position]
        pill.checked = isChecked
        val date = "${CalendarDay.today().year}${CalendarDay.today().month}${CalendarDay.today().day}"
        val key = pill.key
        MyRef.alarmRef.child(userId).child(date).child(key).child("checked").setValue(isChecked)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("HomeFragment", "Pill checked status updated successfully")
                } else {
                    Log.e("HomeFragment", "Error updating pill checked status: ${task.exception}")
                }
            }
    }
    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        val data = itemList.get(position)
        holder.binding.todayCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("##INFO", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    val userId = user.id.toString()
                    updatePillCheckStatus(position, userId, isChecked)
                }
            }
            val textCheck = ContextCompat.getColor(context, R.color.textgray)
            val textnCheck = ContextCompat.getColor(context, R.color.black)
            if(isChecked){
                holder.binding.todayTime.setTextColor(textCheck)
                holder.binding.todayPill.setTextColor(textCheck)
                holder.binding.todayText.setTextColor(textCheck)
            }else{
                holder.binding.todayTime.setTextColor(textnCheck)
                holder.binding.todayPill.setTextColor(textnCheck)
                holder.binding.todayText.setTextColor(textnCheck)
            }
        }
        holder.binding.run {
            todayPill.text = data.mediName
            todayTime.text = "${data.hour} : ${data.minute}"
            todayCheck.isChecked = data.checked
        }
    }
}