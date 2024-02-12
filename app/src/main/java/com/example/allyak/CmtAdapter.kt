package com.example.allyak


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ItemCmtBinding

class CmtViewHolder(val binding: ItemCmtBinding) : RecyclerView.ViewHolder(binding.root)
class CmtAdapter(val context: Context, val itemList: MutableList<Comments>): RecyclerView.Adapter<CmtViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CmtViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CmtViewHolder(ItemCmtBinding.inflate(layoutInflater))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: CmtViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {   //커뮤니티 뷰
            cmtContent.text = data.content
            cmtTime.text = data.time
        }
        // 권한이 있다면 삭제, 닫기
        // 없다면 닫기만
        holder.binding.cmtEdit.setOnClickListener{
            val args = Bundle()
            args.putString("postkey", data.postId)
            args.putString("key", data.docId)
            args.putString("uid", data.userId)
            val bottomSheet = CmtBottomSheet()
            bottomSheet.setArguments(args)
            bottomSheet.show(
                (context as FragmentActivity).supportFragmentManager,
                bottomSheet.getTag()
            )
        }
    }
}