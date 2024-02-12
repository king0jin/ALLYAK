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
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Allyakk", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                userId = user.id.toString()
            }
        }
        val postkey = intent.getStringExtra("key").toString()
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.commentRecyclerView.adapter = CmtAdapter(this, itemList)
        getCmtData(postkey)
        binding.viewTitle.text = intent.getStringExtra("title")
        binding.viewTime.text = intent.getStringExtra("viewdate")
        binding.viewContent.text = intent.getStringExtra("content")
        binding.viewLikeCount.text = intent.getStringExtra("likeCnt")
        binding.viewCommentCount.text = intent.getStringExtra("commentCnt")
        //if click viewLike, likeCount + 1 and image change to ic_fillheart
        binding.viewLike.setOnClickListener {
            // 여기 수정 필요
            var likeCount = binding.viewLikeCount.text.toString().toInt()
            likeCount ++
            binding.viewLike.setImageResource(R.drawable.ic_fillheart)
            binding.viewLikeCount.text = likeCount.toString()
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
                            PostRef.contentRef.child(postkey).child("commentCnt")
                                .setValue(currentCommentCnt + 1)
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