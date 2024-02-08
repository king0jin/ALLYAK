package com.example.allyak

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostRef {
    companion object {
        private val database = Firebase.database
        val contentRef = database.getReference("posts")
    }
}