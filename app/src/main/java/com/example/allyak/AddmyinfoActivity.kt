package com.example.allyak

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AddmyinfoActivity : AppCompatActivity() {
    lateinit var tb : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmyinfo)
        tb = findViewById(R.id.toolbar)
        tb.setTitle("")
        setSupportActionBar(tb)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.pilllist_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}