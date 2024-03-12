package com.example.allyak

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.allyak.databinding.ActivityAddpostBinding
import com.kakao.sdk.user.UserApiClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddpostActivity : AppCompatActivity() {
    //add post를 하면 프래그먼트가 업데이트 되어야 됨
    //누르면 바로 업데이트가 안되고, 프래그먼트를 다시 불러와야 됨
    lateinit var tb: Toolbar
    lateinit var binding: ActivityAddpostBinding
    private lateinit var userId :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tb = findViewById(R.id.toolbar)
        tb.setTitle("")   //delete toolbar title
        setSupportActionBar(tb)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addpost_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveBtn -> {
                //saveBtn 클릭 시 동작
                //데이터 파이어 베이스에 저장 후 액티비티 종료
                //제목, 사용자, 시간, 내용 저장
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("Allyakk", "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        userId = user.id.toString()
                        if (binding.postTitle.text.isNotEmpty() && binding.postContent.text.isNotEmpty()) {
                            //파이어베이스에 저장
                            saveStore()
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

    private fun saveStore() {
        val title = binding.postTitle.text.toString()
        val content = binding.postContent.text.toString()
        val date = dateToString(Date())
        // setValue() 메서드를 사용하여 값을 저장
        // realtime database에 저장
        val key = MyRef.contentRef.push().key.toString()   //랜덤한 문자열, 타임스탬프
        MyRef.contentRef.child(key).setValue(ItemData(key, userId, title, content, date,""))
    }
    private fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale("ko", "KR"))
        return format.format(date)
    }
}