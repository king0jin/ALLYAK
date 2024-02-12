package com.example.allyak

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient

class CmtBottomSheet: BottomSheetDialogFragment() {
    lateinit var closeBtn: TextView
    lateinit var delBtn: TextView
    lateinit var line: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cmt_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mArgs = arguments
        var cmtkey = mArgs!!.getString("key").toString()
        var uid = mArgs!!.getString("uid")
        var postkey = mArgs!!.getString("postkey").toString()
        delBtn = view.findViewById(R.id.bottomSheetDel)
        line = view.findViewById(R.id.bottomSheetLine)
        closeBtn = view.findViewById(R.id.bottomSheetClose)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Allyakk", "사용자 정보 요청 실패", error)
            } else if (user != null && cmtkey != null) {
                if (uid == user.id.toString()) {   //권한을 갖고 있는 경우
                    delBtn.visibility = View.VISIBLE
                    line.visibility = View.VISIBLE
                }
            }
        }
        delBtn.setOnClickListener {
            PostRef.contentRef.child(postkey).child("comments").child(cmtkey).removeValue()
            PostRef.contentRef.child(postkey).child("commentCnt")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val currentCommentCnt = dataSnapshot.getValue(Int::class.java) ?: 0
                        PostRef.contentRef.child(postkey).child("commentCnt")
                            .setValue(currentCommentCnt - 1)
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "Firebase",
                            "Error updating comment count",
                            databaseError.toException()
                        )
                    }
                })
            val cmtCnt = (context as? ViewpostActivity)?.findViewById<TextView>(R.id.viewCommentCount)
            cmtCnt?.text = (cmtCnt?.text.toString().toInt() - 1).toString()
            // 댓글 삭제
            dismiss()
        }
        closeBtn.setOnClickListener {
            dismiss()   //dialog 닫기
        }
    }
}