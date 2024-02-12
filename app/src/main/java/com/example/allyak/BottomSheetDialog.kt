package com.example.allyak

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.user.UserApiClient

class BottomSheetDialog(context: Context) : BottomSheetDialogFragment() {
    lateinit var closeBtn: TextView
    lateinit var editBtn: TextView
    lateinit var delBtn: TextView
    lateinit var line1: View
    lateinit var line2: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeBtn = view.findViewById(R.id.bottomSheetClose)
        editBtn = view.findViewById(R.id.bottomSheetEdit)
        delBtn = view.findViewById(R.id.bottomSheetDel)
        line1 = view.findViewById(R.id.bottomSheetLine1)
        line2 = view.findViewById(R.id.bottomSheetLine2)
        val postkey = arguments?.getString("key")
        val uid = arguments?.getString("uid")
        val title = arguments?.getString("title")
        val content = arguments?.getString("content")
        val date = arguments?.getString("date")
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Allyakk", "사용자 정보 요청 실패", error)
            } else if (user != null && postkey != null) {
                if (uid == user.id.toString()) {   //권한을 갖고 있는 경우
                    editBtn.visibility = View.VISIBLE
                    delBtn.visibility = View.VISIBLE
                    line1.visibility = View.VISIBLE
                    line2.visibility = View.VISIBLE
                }
            }
        }
        delBtn.setOnClickListener {
            PostRef.contentRef.child(postkey.toString()).removeValue()
            // MainActivity로 이동하여 CommunityFragment로 전환
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("destination", "community")
            startActivity(intent)
            dismiss()
        }
        editBtn.setOnClickListener {
            val intent = Intent(this.requireContext(), EditpostActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("content", content)
            intent.putExtra("key", postkey)
            intent.putExtra("uid", uid)
            intent.putExtra("date", date)
            startActivity(intent)
            dismiss()
        }
        closeBtn.setOnClickListener {
            dismiss()   //dialog 닫기
        }
    }
}