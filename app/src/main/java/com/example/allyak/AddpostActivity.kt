package com.example.allyak

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AddpostActivity : AppCompatActivity()  {
    lateinit var tb : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpost)
        tb = findViewById(R.id.toolbar)
        tb.setTitle("")   //delete toolbar title
        setSupportActionBar(tb)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.addpost_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.saveBtn -> {
                //saveBtn 클릭 시 동작
                //데이터 파이어 베이스에 저장 후 액티비티 종료
                //제목, 사용자, 시간, 내용 저장

                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}