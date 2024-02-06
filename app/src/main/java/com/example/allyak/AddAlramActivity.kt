package com.example.allyak

//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Intent

class AddAlramActivity : AppCompatActivity() {

    lateinit var timePicker: TimePicker
    lateinit var alramMediName: EditText
    //lateinit var firebaseAuth: FirebaseAuth
    //lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        timePicker = findViewById(R.id.timePicker)
        alramMediName = findViewById(R.id.alrammediname)

        //firebaseAuth = FirebaseAuth.getInstance()
        //database = FirebaseDatabase.getInstance()

        val addAlarmButton: Button = findViewById(R.id.add)
        val cancelAlarmButton: Button = findViewById(R.id.cancel2)

        addAlarmButton.setOnClickListener {
            //알람설정
            setAlram()
        }
        cancelAlarmButton.setOnClickListener {
            // AddAlarmActivity 종료
            finish()


        }


    }
    private fun setAlram() {
        val hour = timePicker.hour
        val minute = timePicker.minute
        val mediName = alramMediName.text.toString()

        //알람 시간 설정
        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, minute)
        calender.set(Calendar.SECOND, 0)

        //알람 정보를 저장할 경로
        // val alramRef = database.getReference("alarms")
        // val alram = Alram(hour, minute, mediName)
        // alramRef.push().setValue(alram)

        // 알람이 울릴 때 실행될 동작을 수행할 BroadcastReceiver지정
        //val alarmIntent = Intent(this, AlarmReceiver::class.java)
        //약이름도 전달
        //alarmIntent.putExtra("mediName", mediName)

        //val pendingIntent = PendingIntent.getBroadcast(
    //        this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //알람매니저를 사용하여 알람 설정
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager.set(
//            AlarmManager.RTC_WAKEUP,
//            calender.timeInMillis,
//            pendingIntent
//        )

        // 알람정보를 Firebase에 저장
        //saveAlarmToFirebase(firebaseAuth.currentUser!!.uid, hour, minute, mediName)

        //알람 설정 후 추가적이 동작
        // 알람이 설정되었다라는 메세지 표시
    }
    //private fun saveAlarmToFirebase(userId:String?, hour:Int, minute:Int, mediName:String) {
        // 파이어베이스에 알람 정보 저장
        //userId?.let {
        //    val alramInfo = AlramInfo(hour, minute, mediName)
        //    val alramRef = database.getReference("alarms").child(it)
        //    alramRef.push().setValue(alramInfo)
        //}

}