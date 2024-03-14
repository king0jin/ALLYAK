package com.example.allyak

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ItemInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class InfoViewHolder(val binding: ItemInfoBinding) : RecyclerView.ViewHolder(binding.root)
class InfoAdapter(val context: Context, val itemList: MutableList<MyInfo>, val recyclerViewType: String): RecyclerView.Adapter<InfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InfoViewHolder(ItemInfoBinding.inflate(layoutInflater))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    private fun removeInfo(position: Int, userId: String, recyclerType: String) {
        val info = itemList[position]
        Log.d("Allyakkk", "info: ${info}")
        val key = info.key.toString()
        if(recyclerType == "medi"){
            MyRef.infoRef.child(userId).child(key).child("medinow").setValue(false)
                .addOnSuccessListener {
                    removeCheck(userId, key)
                }
        }
        else if(recyclerType == "side"){
            MyRef.infoRef.child(userId).child(key).child("medinot").setValue(false)
                .addOnSuccessListener {
                    removeCheck(userId, key)
                }
        }
    }
    private fun removeCheck(userId: String, key: String){
        MyRef.infoRef.child(userId).child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val medinow = snapshot.child("medinow").getValue(Boolean::class.java) ?: false
                val medinot = snapshot.child("medinot").getValue(Boolean::class.java) ?: false
                if (!medinow && !medinot) {
                    snapshot.ref.removeValue()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "removeCheck:onCancelled", error.toException())
            }
        })
    }
    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val data = itemList.get(position)
        holder.binding.run {
            infoName.text = data.pillName
            infoMemo.text = data.pillMemo
        }
        holder.itemView.setOnClickListener{
            val dlg = AlertDialog.Builder(holder.itemView.context)
            dlg.setTitle("약 삭제")
            dlg.setMessage("삭제하시겠습니까?")
            dlg.setPositiveButton("삭제") { dialog, which ->
                UserApiClient.instance.me { user, error ->
                    removeInfo(position, user?.id.toString(), recyclerViewType)
                }
            }
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
    }
}