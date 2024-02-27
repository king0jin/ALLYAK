package com.example.allyak

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PillListAdapter(var pills : ArrayList<PillListInfo>, var callback :
OnItemClickListener) : RecyclerView.Adapter<PillListAdapter.PillListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PillListViewHolder {
        return PillListViewHolder(View.inflate(parent.context, R.layout.item_pill, null))

    }

    override fun onBindViewHolder(holder: PillListViewHolder, position: Int) {
        holder.mediName.text = pills[position].mediName
        holder.time.text = "${pills[position].hour} : ${pills[position].minute}"
        holder.check.isChecked = pills[position].checked

        holder.check.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.i("##INFO", "ischecked = ${isChecked}")
//            if(isChecked) {
//                // 체크박스가 체크되면 isCheck를 true로 변경
//                pills[position].checked = true
//            } else {
//                // 체크박스가 해제되면 isCheck를 false로 변경
//                pills[position].checked = false
//            }
            pills[position].checked = isChecked

            callback.onItemClick(pills[position], position)
        }

    }
    override fun getItemCount(): Int {
        return pills.size
    }

    fun updateItems(pills: ArrayList<PillListInfo>) {
        this.pills = pills
        notifyDataSetChanged()
    }

    class PillListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mediName = itemView.findViewById<TextView>(R.id.tv_pill)
        val time = itemView.findViewById<TextView>(R.id.tv_time)
        val check = itemView.findViewById<CheckBox>(R.id.check_pill)
    }

    interface OnItemClickListener {
        fun onItemClick(data: PillListInfo, position: Int)
    }

}