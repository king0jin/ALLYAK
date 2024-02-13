package com.example.allyak

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allyak.databinding.ActivityViewpostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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
        val postkey = intent.getStringExtra("key").toString()
        val likeRef = PostRef.contentRef.child(postkey).child("like")
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
        binding.viewTitle.text = intent.getStringExtra("title")
        binding.viewTime.text = intent.getStringExtra("date")
        binding.viewContent.text = intent.getStringExtra("content")
        binding.viewLikeCount.text = intent.getStringExtra("likeCnt")
        binding.viewCommentCount.text = intent.getStringExtra("commentCnt")
        binding.viewLike.setOnClickListener {
            //클릭 시 like 데이터베이스에 유저 아이디 저장
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
            PostRef.contentRef.child(postkey).child("likeCnt")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val currentLikeCnt = dataSnapshot.getValue(Int::class.java) ?: 0
                        if (existId1) { //이미 누른 경우
                            binding.viewLikeCount.text = (binding.viewLikeCount.text.toString().toInt() - 1).toString()
                            binding.viewLike.setImageResource(R.drawable.ic_heart)
                            PostRef.contentRef.child(postkey).child("likeCnt").setValue(currentLikeCnt - 1)
                        } else { //누른 적이 없는 경우
                            binding.viewLikeCount.text = (binding.viewLikeCount.text.toString().toInt() + 1).toString()
                            binding.viewLike.setImageResource(R.drawable.ic_fillheart)
                            PostRef.contentRef.child(postkey).child("likeCnt").setValue(currentLikeCnt + 1)
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
        binding.viewEdit.setOnClickListener{
            val bundle = Bundle().apply {
                putString("key", postkey)
                putString("uid", intent.getStringExtra("uid"))
                putString("title", intent.getStringExtra("title"))
                putString("content", intent.getStringExtra("content"))
                putString("date", intent.getStringExtra("date"))
            }
            val bottomSheet = BottomSheetDialog(this)
            bottomSheet.arguments = bundle
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        binding.writeCommentBtn.setOnClickListener{
            if (binding.writeComment.text.isNotEmpty()) {
                val cmt = binding.writeComment.text.toString()
                val date = dateToString(Date())
                // Firebase Realtime Database의 경로 설정
                // ********
                val commentsRef = FirebaseDatabase.getInstance().getReference("posts/$postkey/comments")
                // 댓글의 고유 ID를 생성
                val newCommentId = commentsRef.push().key
                // 새로운 댓글 객체 생성
                val newComment = Comments(postkey, newCommentId, userId, cmt, date)
                commentsRef.child(newCommentId!!).setValue(newComment)
                binding.viewCommentCount.text = (binding.viewCommentCount.text.toString().toInt() + 1).toString()
                binding.writeComment.text.clear()
                // 기존 포스트의 commentCnt 증가
                PostRef.contentRef.child(postkey).child("commentCnt")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val currentCommentCnt = dataSnapshot.getValue(Int::class.java) ?: 0
                            PostRef.contentRef.child(postkey).child("commentCnt").setValue(currentCommentCnt + 1)
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
                    Log.d("Allyakk", "item: ${item}")
                }
                // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
                (binding.commentRecyclerView.adapter as CmtAdapter).notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Allyakk", "loadPost:onCancelled", error.toException())
            }
        }
        // addValueEventListener() 메서드로 DatabaseReference에 ValueEventListener를 추가한다.
        PostRef.contentRef.child(postkey).child("comments").addValueEventListener(postListener)
    }
    private fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ko", "KR"))
        return format.format(date)
    }
}