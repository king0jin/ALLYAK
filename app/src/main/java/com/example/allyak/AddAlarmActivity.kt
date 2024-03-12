package com.example.allyak

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var userId :String
    lateinit var timePicker: TimePicker
    lateinit var alarmMediName: EditText
    lateinit var add: Button
    lateinit var cancel2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        timePicker = findViewById(R.id.timePicker)
        alarmMediName = findViewById(R.id.alarmmediname)
        add = findViewById(R.id.add)
        cancel2 = findViewById(R.id.cancel2)

        add.setOnClickListener {
            //알람설정 정보 저장
            val date = intent.getStringExtra("date").toString()
            val year = intent.getIntExtra("selectedYear", 0)
            val month = intent.getIntExtra("selectedMonth", 0)
            val dayOfMonth = intent.getIntExtra("selectedDayOfMonth", 0)

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    //사용자 정보 요청 실패
                    Log.d("alarm", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    userId = user.id.toString()
                    if (alarmMediName.text.isNotEmpty()) {
                        //파이어베이스에 저장
                        setAlarm(date, year, month, dayOfMonth)
                        finish()
                    } else {
                        Toast.makeText(this, "약 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        cancel2.setOnClickListener {
            // AddAlarmActivity 종료
            finish()
        }
    }

    private fun setAlarm(date : String, year: Int, month: Int, dayOfMonth: Int) {
        val hour = timePicker.hour
        val minute = timePicker.minute
        val mediName = alarmMediName.text.toString()

        //데이터 베이스에 저장할 데이터 맵 생성
        val alarms = hashMapOf(
            "hour" to hour,
            "minute" to minute,
            "mediName" to mediName,
            "calendarYear" to year,
            "calendarMonth" to month,
            "calendarDay" to dayOfMonth,
            "checked" to false
        )
        //데이터 베이스에 데이터 쓰기
        //수수정
        //alarms - key - alarmsMap
        val key = "$hour$minute"+MyRef.alarmRef.push().key.toString() //알람을 정렬하기 위한 키 설정
        MyRef.alarmRef.child(userId).child("$year$month$dayOfMonth").child(key).setValue(alarms)
        //MyRef.alarmRef.push().setValue(alarms)
            .addOnSuccessListener {
                Log.d("alarm", "알람 정보 저장 성공")
            }
            .addOnFailureListener {
                Log.d("alarm", "알람 정보 저장 실패")
            }
    }

}