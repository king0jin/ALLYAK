package com.example.allyak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.allyak.databinding.ActivityAddpostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class EditpostActivity : AppCompatActivity() {
    lateinit var tb: Toolbar
    lateinit var binding: ActivityAddpostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tb = findViewById(R.id.toolbar)
        tb.setTitle("")   //delete toolbar title
        setSupportActionBar(tb)
        //intent로 받아온 데이터를 뷰에 띄워준다.
        //여기서는 intent.getStringExtra 해도 될 듯 ..?
        binding.postTitle.setText(intent.getStringExtra("title"))
        binding.postContent.setText(intent.getStringExtra("content"))
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addpost_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val postkey = intent.getStringExtra("key")
        when (item.itemId) {
            R.id.saveBtn -> {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("Allyakk", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null && postkey != null){
                        if (binding.postTitle.text.isNotEmpty() && binding.postContent.text.isNotEmpty()) {
                            //파이어베이스에 업데이트
                            editStore(postkey)
                            finish()
                        } else {
                            Toast.makeText(this, "제목과 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun editStore(key : String) {
        val title = binding.postTitle.text.toString()
        val content = binding.postContent.text.toString()
        // realtime database에 저장
        // 해당 게시물의 기존 데이터를 불러오기
        val postRef = MyRef.contentRef.child(key)
        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 기존 데이터를 가져와서 ItemData 객체로 매핑
                val existingData = snapshot.getValue(ItemData::class.java)
                // 기존 데이터가 null이 아니라면 업데이트 수행
                existingData?.let {
                    it.title = title
                    it.content = content
                    // 기존 데이터를 다시 저장
                    postRef.setValue(it)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching existing data", error.toException())
            }
        })
    }
}