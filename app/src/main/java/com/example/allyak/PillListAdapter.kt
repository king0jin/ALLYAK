package com.example.allyak

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PillListAdapter(var pills : ArrayList<PillListInfo>, val callback : OnItemClickListener) : RecyclerView.Adapter<PillListAdapter.PillListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PillListViewHolder {
        return PillListViewHolder(View.inflate(parent.context, R.layout.item_pill, null))
    }

    override fun onBindViewHolder(holder: PillListViewHolder, position: Int) {
        lateinit var newhour: String
        lateinit var newminute: String
        if(pills[position].hour < 10) newhour="0${pills[position].hour}"
        else newhour="${pills[position].hour}"
        if(pills[position].minute < 10) newminute="0${pills[position].minute}"
        else newminute = "${pills[position].minute}"
        holder.mediName.text = pills[position].mediName
        holder.time.text = "${newhour} : ${newminute}"
        holder.check.isChecked = pills[position].checked

        holder.check.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.i("##INFO", "ischecked = ${isChecked}")
            pills[position].checked = isChecked
            callback.onItemClick(pills[position], position)
        }
        holder.deleteView.setOnClickListener {
            callback.onItemDelete(pills[position], position)
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
        val deleteView = itemView.findViewById<ImageView>(R.id.im_delete)
    }

    interface OnItemClickListener {
        fun onItemClick(data: PillListInfo, position: Int)
        fun onItemDelete(data: PillListInfo, position: Int)
    }

}