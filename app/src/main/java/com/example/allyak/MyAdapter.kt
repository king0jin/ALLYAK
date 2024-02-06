package com.example.allyak

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ItemCommunityBinding

class MyViewHolder(val binding: ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root)
class MyAdapter(val context: Context, val itemList: MutableList<ItemData>): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemCommunityBinding.inflate(layoutInflater))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            communityTitle.text = data.title
            communityTime.text = data.time
            communityLike.text = data.like
            communityCmt.text = data.comment
        }
    }
}