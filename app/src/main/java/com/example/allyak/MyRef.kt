package com.example.allyak

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyRef {
    companion object {
        private val database = Firebase.database
        val contentRef = database.getReference("posts")
        val reviewRef = database.getReference("reviews")
        val alarmRef = database.getReference("alarms")
        val infoRef = database.getReference("myinfo")
    }
}