package com.example.allyak


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ItemCmtBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

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
        val itemView = holder.itemView
        holder.binding.run {   //커뮤니티 뷰
            cmtContent.text = data.content
            cmtTime.text = data.time
        }
        holder.binding.cmtEdit.setOnClickListener{
            val args = Bundle()
            val postkey = data.postId.toString()
            val docId = data.docId.toString()
            val userId = data.userId
            args.putString("postkey", postkey)
            args.putString("key", docId)
            args.putString("uid", userId)
            val popupMenu = PopupMenu(itemView.context, itemView)
            popupMenu.inflate(R.menu.cmtpopup_menu)
            // 팝업 메뉴의 아이템을 클릭했을 때의 동작을 정의
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popDel -> {
                        MyRef.contentRef.child(postkey).child("comments").child(docId).removeValue()
                        MyRef.contentRef.child(postkey).child("commentCnt")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val currentCommentCnt = dataSnapshot.getValue(Int::class.java) ?: 0
                                    MyRef.contentRef.child(postkey).child("commentCnt")
                                        .setValue(currentCommentCnt - 1)
                                }
                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.e(
                                        "Firebase",
                                        "Error updating comment count",
                                        databaseError.toException()
                                    )
                                }
                            })
                        val cmtCnt = (context as? ViewpostActivity)?.findViewById<TextView>(R.id.viewCommentCount)
                        cmtCnt?.text = (cmtCnt?.text.toString().toInt() - 1).toString()
                        true
                    }
                    R.id.popClose -> {
                        popupMenu.dismiss()
                        true
                    }
                    else -> false
                }
            }
            // 팝업 메뉴를 표시
            popupMenu.show()
            // 유저 정보를 통해 팝업 메뉴의 항목을 제어
            UserApiClient.instance.me { user, error ->
                if (error == null && user != null) {
                    val myUid = user.id.toString()
                    // 버튼을 누른 사용자의 uid와 내 uid를 비교하여 팝업 메뉴의 항목을 제어
                    if (myUid == userId) {
                        // 내 uid와 같으면 item1, item2, item3 모두 표시
                    } else {
                        popupMenu.menu.removeItem(R.id.popDel)
                    }
                }
            }
        }
    }
}