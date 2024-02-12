package com.example.allyak

import android.content.Context
import android.content.Intent
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

        holder.binding.run {   //커뮤니티 뷰
            communityTitle.text = data.title
            communityTime.text = data.viewdate
            communityLike.text = data.likeCnt.toString()
            communityCmt.text = data.commentCnt.toString()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ViewpostActivity::class.java)
            //근데 굳이 데이터를 전송해야되는지 ... ?
            //그냥 키를 갖고 가서 해당 페이지에서 데이터를 불러오는게 더 효율적일 수도 있음
            intent.putExtra("key", data.docId)
            intent.putExtra("uid", data.UID)
            intent.putExtra("title", data.title)
            intent.putExtra("content", data.content)
            intent.putExtra("date", data.date)
            intent.putExtra("viewdate", data.viewdate)
            intent.putExtra("likeCnt", data.likeCnt.toString())
            intent.putExtra("commentCnt", data.commentCnt.toString())
            context.startActivity(intent)
        }
    }
}