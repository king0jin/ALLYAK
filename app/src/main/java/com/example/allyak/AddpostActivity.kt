package com.example.allyak

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.allyak.databinding.ActivityAddpostBinding
import java.text.SimpleDateFormat
import java.util.Date

class AddpostActivity : AppCompatActivity() {
    lateinit var tb: Toolbar
    lateinit var binding: ActivityAddpostBinding  //필요한 이유 ...?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_addpost)
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
                if (binding.postTitle.text.isNotEmpty() && binding.postContent.text.isNotEmpty()) {
                    //파이어베이스에 저장
                    saveStore()
                    //액티비티 종료
                    finish()
                } else {
                    Toast.makeText(this, "제목과 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveStore() {
        //add............................
        val data = mapOf(
            "title" to binding.postTitle.text.toString(),
            "content" to binding.postContent.text.toString(),
            "date" to dateToString(Date()),
            "like" to "0",
            "comment" to "0"
        )

        MyApplication.db.collection("post")
            .add(data)
            .addOnSuccessListener {
                Log.d("Allyak", "data save success")
            }
            .addOnFailureListener {
                Log.d("Allyak", "data save error", it)
            }

    }
    private fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy/MM/dd")
        return format.format(date)
    }
}