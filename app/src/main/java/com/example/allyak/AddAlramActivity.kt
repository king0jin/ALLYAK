package com.example.allyak

//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.kakao.sdk.user.UserApiClient

//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Intent

class AddAlramActivity : AppCompatActivity() {
    private lateinit var userId :String
    lateinit var timePicker: TimePicker
    lateinit var alramMediName: EditText
    lateinit var add: Button
    lateinit var cancel2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        timePicker = findViewById(R.id.timePicker)
        alramMediName = findViewById(R.id.alrammediname)
        add = findViewById(R.id.add)
        cancel2 = findViewById(R.id.cancel2)

        add.setOnClickListener {
            //알람설정 정보 저장
            val year = intent.getIntExtra("selectedYear", 0)
            val month = intent.getIntExtra("selectedMonth", 0)
            val dayOfMonth = intent.getIntExtra("selectedDayOfMonth", 0)

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    //사용자 정보 요청 실패
                    Log.d("alarm", "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    userId = user.id.toString()
                    if (alramMediName.text.isNotEmpty()) {
                        //파이어베이스에 저장
                        setAlarm(year, month, dayOfMonth)
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

    private fun setAlarm(year: Int, month: Int, dayOfMonth: Int) {
        //firebase데이터베이스 레퍼런스
        val database = FirebaseDatabase.getInstance()
        val alarmRef = database.getReference("alarms")

        val hour = timePicker.hour
        val minute = timePicker.minute
        val mediName = alramMediName.text.toString()

        //데이터 베이스에 저장할 데이터 맵 생성
        val alarms = hashMapOf(
            "userId" to userId,
            "hour" to hour,
            "minute" to minute,
            "mediName" to mediName,
            "calendarYear" to year,
            "calendarMonth" to month,
            "calendarDay" to dayOfMonth,
            "checked" to false
        )

        //데이터 베이스에 데이터 쓰기
        alarmRef.push().setValue(alarms)
            .addOnSuccessListener {
                Log.d("alarm", "알람 정보 저장 성공")
            }
            .addOnFailureListener {
                Log.d("alarm", "알람 정보 저장 실패")
            }
    }

}