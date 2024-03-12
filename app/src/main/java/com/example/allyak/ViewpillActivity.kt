package com.example.allyak

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.allyak.databinding.ActivityViewpillBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewpillActivity:AppCompatActivity() {
    private val binding by lazy{ ActivityViewpillBinding.inflate(layoutInflater)}
    lateinit var userId :String
    private val reviewsReference = MyRef.reviewRef
    private val symptoms = listOf("sym0","sym1","sym2","sym3","sym4","sym5","sym6","sym7","sym8","sym9","sym10","sym11")

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                Glide.with(binding.root.context)
                    .load(intent.getStringExtra("image"))
                    .into(binding.pillImg)
                Log.d("Allyak", "bitmap")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Allyak", "error: $e")
            }
        }
        val itemNum = intent.getStringExtra("itemSeq").toString() //품목 일련번호
        checkIfItemNumExists(itemNum)
        binding.pillName.text = intent.getStringExtra("itemName")
        binding.entpName.text = intent.getStringExtra("entpName")
        binding.efcyQesitm.text = intent.getStringExtra("efcyQesitm")
        binding.useMethod.text = intent.getStringExtra("useMethodQesitm")
        binding.useWarning.text = intent.getStringExtra("atpnQesitm")
        binding.depositMethod.text = intent.getStringExtra("deposit")
        binding.sideEffect.text = intent.getStringExtra("seQesitm")
        if(intent.getStringExtra("atpnWarnQesitm") == "null"){
            binding.warningPill.text = ""
        } else {
            binding.warningPill.text = intent.getStringExtra("atpnWarnQesitm")
        }
        binding.mvPillinfo.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 터치 시작할 때 색상 변경
                    binding.mvPillinfo.setTextColor(Color.GRAY)
                    binding.scrollView.smoothScrollTo(0, binding.pillInfo.top)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 터치 종료 또는 취소될 때 색상 원래대로 변경
                    binding.mvPillinfo.setTextColor(Color.BLACK)
                }
            }
            true
        }
        binding.mvReview.setOnTouchListener{ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 터치 시작할 때 색상 변경
                    binding.mvReview.setTextColor(Color.GRAY)
                    binding.scrollView.smoothScrollTo(0, binding.pillReview.top)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // 터치 종료 또는 취소될 때 색상 원래대로 변경
                    binding.mvReview.setTextColor(Color.BLACK)
                }
            }
            true
        }
        //realtime 을 snapshot으로 다 보고 해당 일련번호가 있는지 확인. 없으면 생성, 있으면 count 불러오기
        //가능하면 얘를 리스트로 동적 처리, selector되는지 확인, 텍컬 white로 변경
        for (symptom in symptoms){
            val symptomTag = findViewById<LinearLayout>(resources.getIdentifier(symptom, "id", packageName))
            val symptomName = findViewById<TextView>(resources.getIdentifier(symptom+"n", "id", packageName))
            val symptomCnt = findViewById<TextView>(resources.getIdentifier(symptom+"Cnt", "id", packageName))
            symptomTag.setOnClickListener {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("Allyakk", "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        userId = user.id.toString()
                        var existId1 = false
                        reviewsReference.child(itemNum).child(symptom).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (data in dataSnapshot.children) {
                                        val item = data.getValue(ReviewData::class.java) //uid 형식만 빌리는 것
                                        if (userId == item?.uid) existId1 = true
                                    }
                                    if (existId1) { //이미 누른 경우
                                        reviewsReference.child(itemNum).child(symptom).child(userId).removeValue()
                                        symptomTag.setBackgroundResource(R.drawable.tag_rect)
                                        symptomName.setTextColor(Color.BLACK)
                                        symptomCnt.setTextColor(Color.BLACK)
                                        symptomCnt.text = (symptomCnt.text.toString().toInt() - 1).toString()
                                    } else {
                                        reviewsReference.child(itemNum).child(symptom).child(userId!!).setValue(ReviewData(userId))
                                        symptomTag.setBackgroundResource(R.drawable.tag_rect_on)
                                        symptomName.setTextColor(Color.WHITE)
                                        symptomCnt.setTextColor(Color.WHITE)
                                        symptomCnt.text = (symptomCnt.text.toString().toInt() + 1).toString()
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.e(
                                        "Firebase",
                                        "Error updating comment count",
                                        databaseError.toException()
                                    )
                                }
                            })

                    }
                }
            }
        }
    }
    private fun checkIfItemNumExists(itemNum : String) { //itemSeq에 대한 데이터가 존재하는지 아닌지 확인
        reviewsReference.child(itemNum).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // itemNum이 이미 존재하면 기존 데이터 로드
                    loadExistingData(itemNum)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // onCancelled 이벤트 처리
            }
        })
    }
    private fun loadExistingData(itemNum : String) { //존재하는 경우
        // 각 증상 카운트 TextView를 루프 돌면서 텍스트 업데이트
        for (symptom in symptoms){
            val symptomTag = findViewById<LinearLayout>(resources.getIdentifier(symptom, "id", packageName))
            val symptomName = findViewById<TextView>(resources.getIdentifier(symptom+"n", "id", packageName))
            val symptomCnt = findViewById<TextView>(resources.getIdentifier(symptom+"Cnt", "id", packageName))
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("Allyakk", "사용자 정보 요청 실패", error)
                }
                else if (user != null) {
                    userId = user.id.toString()
                    var existId = false
                    reviewsReference.child(itemNum).child(symptom).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for(data in dataSnapshot.children){
                                for(data in dataSnapshot.children){
                                    val item = data.getValue(ReviewData::class.java)
                                    if(userId == item?.uid) existId = true
                                }
                            }
                            if (existId) { //이미 누른 경우
                                symptomTag.setBackgroundResource(R.drawable.tag_rect_on)
                                symptomName.setTextColor(Color.WHITE)
                                symptomCnt.setTextColor(Color.WHITE)
                            }
                            symptomCnt.text = dataSnapshot.childrenCount.toString()
                            Log.d("allyakkk", "count 실행")
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e("Firebase", "Error updating comment count", databaseError.toException())
                        }
                    })
                }
            }
        }
    }
}
