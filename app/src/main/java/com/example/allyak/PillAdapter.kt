package com.example.allyak

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import com.bumptech.glide.Glide
import com.example.allyak.databinding.ItemPilllistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PillAdapter(val items:MutableList<Item?>): RecyclerView.Adapter<PillAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PillAdapter.ViewHolder {
        val binding = ItemPilllistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PillAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    override fun getItemCount(): Int {
        return items.size
    }
    inner class ViewHolder(val binding: ItemPilllistBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                val currentItem = items[absoluteAdapterPosition]
                val intent = Intent(binding.root.context, ViewpillActivity::class.java)
                intent.putExtra("itemName", currentItem?.itemName) //제품 이름
                intent.putExtra("entpName", currentItem?.entpName) //기업 이름
                intent.putExtra("itemSeq", currentItem?.itemSeq) //품목 일련번호
                intent.putExtra("efcyQesitm", currentItem?.efcyQesitm) //효능
                intent.putExtra("useMethodQesitm", currentItem?.useMethodQesitm) //사용법
                intent.putExtra("atpnWarnQesitm", currentItem?.atpnWarnQesitm.toString()) //주의사항경고
                intent.putExtra("atpnQesitm", currentItem?.atpnQesitm) //주의사항
                intent.putExtra("seQesitm", currentItem?.seQesitm) //부작용
                intent.putExtra("deposit", currentItem?.depositMethodQesitm) //보관방법
                intent.putExtra("image", currentItem?.itemImage) //이미지
                binding.root.context.startActivity(intent)
            }
        }
        fun bind(item: Item?){
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    Glide.with(binding.root.context)
                        .load(item?.itemImage)
                        .into(binding.pillImg)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("Allyak", "error: $e")
                }
            }
            binding.pillName.text = item?.itemName
            binding.entpName.text = item?.entpName
        }
    }
}