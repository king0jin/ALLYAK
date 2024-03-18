package com.example.allyak

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allyak.databinding.ActivityViewpostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewpostActivity : AppCompatActivity()  {
    lateinit var binding: ActivityViewpostBinding
    lateinit var tb: Toolbar
    lateinit var userId :String
    private val itemList = mutableListOf<Comments>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewpostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tb = findViewById(R.id.toolbar)
        tb.setTitle("")   //delete toolbar title
        setSupportActionBar(tb)
        tb.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        val postkey = intent.getStringExtra("key").toString()
        val likeRef = MyRef.contentRef.child(postkey).child("like")
        val cmtRef =  MyRef.contentRef.child(postkey).child("comments")
        val uid = intent.getStringExtra("uid").toString()
        val title = intent.getStringExtra("title").toString()
        val content = intent.getStringExtra("content").toString()
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Allyakk", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                userId = user.id.toString()
                var existId = false
                likeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for(data in dataSnapshot.children){
                                val item = data.getValue(LikeData::class.java)
                                if(userId == item?.userId) existId = true
                            }
                            if (existId) { //이미 누른 경우
                                binding.viewLike.setImageResource(R.drawable.ic_fillheart)
                            } else { //누른 적이 없는 경우
                                binding.viewLike.setImageResource(R.drawable.ic_heart)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e(
                                "Firebase",
                                "Error updating comment count",
                                databaseError.toException()
                            )
                        }
                    })
            }
        }
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.adapter = CmtAdapter(this, itemList)
        getCmtData(postkey)
        binding.viewTitle.text = title
        binding.viewTime.text = intent.getStringExtra("date")
        binding.viewContent.text = content
        binding.viewLikeCount.text = intent.getStringExtra("likeCnt")
        binding.viewCommentCount.text = intent.getStringExtra("commentCnt")
        binding.viewEdit.setOnClickListener{
            showPopupMenu(binding.viewEdit, postkey, uid, title, content)
        }
        binding.viewLike.setOnClickListener {
            var existId1 = false
            likeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for(data in dataSnapshot.children){
                            val item = data.getValue(LikeData::class.java)
                            if(userId == item?.userId) existId1 = true
                        }
                        if(existId1){
                            likeRef.child(userId).removeValue()
                        }else{
                            likeRef.child(userId!!).setValue(LikeData(userId))
                        }

                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "Firebase",
                            "Error updating comment count",
                            databaseError.toException()
                        )
                    }
                })
            //좋아요 개수 변경
            MyRef.contentRef.child(postkey).child("likeCnt")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val currentLikeCnt = dataSnapshot.getValue(Int::class.java) ?: 0
                        if (existId1) { //이미 누른 경우
                            binding.viewLikeCount.text = (binding.viewLikeCount.text.toString().toInt() - 1).toString()
                            binding.viewLike.setImageResource(R.drawable.ic_heart)
                            MyRef.contentRef.child(postkey).child("likeCnt").setValue(currentLikeCnt - 1)
                        } else { //누른 적이 없는 경우
                            binding.viewLikeCount.text = (binding.viewLikeCount.text.toString().toInt() + 1).toString()
                            binding.viewLike.setImageResource(R.drawable.ic_fillheart)
                            MyRef.contentRef.child(postkey).child("likeCnt").setValue(currentLikeCnt + 1)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "Firebase",
                            "Error updating comment count",
                            databaseError.toException()
                        )
                    }
                })
        }
        binding.writeCommentBtn.setOnClickListener{
            if (binding.writeComment.text.isNotEmpty()) {
                val cmt = binding.writeComment.text.toString()
                val date = dateToString(Date())
                // 댓글의 고유 ID를 생성
                val newCommentId = cmtRef.push().key
                // 새로운 댓글 객체 생성
                val newComment = Comments(postkey, newCommentId, userId, cmt, date)
                cmtRef.child(newCommentId!!).setValue(newComment)
                binding.viewCommentCount.text = (binding.viewCommentCount.text.toString().toInt() + 1).toString()
                binding.writeComment.text.clear()
                // 기존 포스트의 commentCnt 증가
                MyRef.contentRef.child(postkey).child("commentCnt")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val currentCommentCnt = dataSnapshot.getValue(Int::class.java) ?: 0
                            MyRef.contentRef.child(postkey).child("commentCnt").setValue(currentCommentCnt + 1)
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e(
                                "Firebase",
                                "Error updating comment count",
                                databaseError.toException()
                            )
                        }
                    })
            } else{
                Toast.makeText(this, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getCmtData(postkey : String){
        val postListener = object : ValueEventListener { //realtime database 가져오기
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(Comments::class.java)
                    itemList.add(item!!)
                    Log.d("Allyakk", "CmtItem: ${item}")
                }
                // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
                (binding.commentRecyclerView.adapter as CmtAdapter).notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Allyakk", "loadPost:onCancelled", error.toException())
            }
        }
        // addValueEventListener() 메서드로 DatabaseReference에 ValueEventListener를 추가한다.
        MyRef.contentRef.child(postkey).child("comments").addValueEventListener(postListener)
    }
    private fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ko", "KR"))
        return format.format(date)
    }
    private fun showPopupMenu(view: View, postkey: String, uid: String, title: String, content: String) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.popup_menu)
        // 팝업 메뉴의 아이템을 클릭했을 때의 동작을 정의
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.popUpdate -> {
                    val intent = Intent(this, EditpostActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("content", content)
                    intent.putExtra("key", postkey)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.popDel -> {
                    MyRef.contentRef.child(postkey).removeValue()
                    finish()
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
                if (myUid == uid) {
                    // 내 uid와 같으면 item1, item2, item3 모두 표시
                } else {
                    // 내 uid와 다르면 item3만 표시
                    popupMenu.menu.removeItem(R.id.popUpdate)
                    popupMenu.menu.removeItem(R.id.popDel)
                }
            }
        }
    }
}