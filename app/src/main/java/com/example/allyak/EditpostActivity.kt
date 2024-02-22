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
        val userId = intent.getStringExtra("uid").toString()
        val date = intent.getStringExtra("date").toString()
        Log.d("Allyakk", "userId : $userId, date : $date")
        when (item.itemId) {
            R.id.saveBtn -> {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("Allyakk", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null && postkey != null){
                        if (binding.postTitle.text.isNotEmpty() && binding.postContent.text.isNotEmpty()) {
                            //파이어베이스에 업데이트
                            editStore(postkey, userId, date)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("destination", "community")
                            startActivity(intent)
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
    private fun editStore(key : String,userId : String, date : String) {
        val title = binding.postTitle.text.toString()
        val content = binding.postContent.text.toString()
        // setValue() 메서드를 사용하여 값을 저장
        // realtime database에 저장
        PostRef.contentRef.child(key).setValue(ItemData(key, userId, title, content,date,"")) //기존 like, comment도 유지해주게 수정
    }
}