package com.example.allyak

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import java.util.Calendar

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var userId: String
    lateinit var timePicker: TimePicker
    lateinit var alarmMediName: EditText
    lateinit var add: Button
    lateinit var cancel2: Button

    @RequiresApi(Build.VERSION_CODES.O)
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
                        //알람 설정
                        scheduleNotification()
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

    private fun setAlarm(date: String, year: Int, month: Int, dayOfMonth: Int) {
        val hour = timePicker.hour
        val minute = timePicker.minute
        val mediName = alarmMediName.text.toString()
        //
        lateinit var newhour: String
        lateinit var newminute: String
        if (hour < 10) newhour = "0$hour"
        else newhour = "$hour"
        if (minute < 10) newminute = "0$minute"
        else newminute = "$minute"
        val key = "$newhour$newminute" + MyRef.alarmRef.push().key.toString() //알람을 정렬하기 위한 키 설정
        //데이터 베이스에 저장할 데이터 맵 생성
        val alarms = hashMapOf(
            "key" to key,
            "hour" to hour,
            "minute" to minute,
            "mediName" to mediName,
            "calendarYear" to year,
            "calendarMonth" to month,
            "calendarDay" to dayOfMonth,
            "checked" to false
        )
        //데이터 베이스에 데이터 쓰기
        MyRef.alarmRef.child(userId).child("$year$month$dayOfMonth").child(key).setValue(alarms)
            .addOnSuccessListener {
                Log.d("alarm", "알람 정보 저장 성공")
            }
            .addOnFailureListener {
                Log.d("alarm", "알람 정보 저장 실패")
            }
    }


    // 알람
    private var notificationID = ""
    private var channelID = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val notificationTitle = alarmMediName.text.toString()
        val notificationMessage = "${notificationTitle} 복용할 시간입니다."
        // 알림을 여러개 설정하기 위해서 NotificationID를 다르게 설정
        val uniqueNotificationID = System.currentTimeMillis().toInt() // Create a unique ID

        notificationID = uniqueNotificationID.toString()
        channelID = "channel_$uniqueNotificationID"


        createNotificationChannel(channelID)


        intent.putExtra(titleExtra, notificationTitle)
        intent.putExtra(messageExtra, notificationMessage)
        intent.putExtra("channelID", channelID)
        intent.putExtra("notificationID", notificationID)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            uniqueNotificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun getTime(): Long {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = intent.getIntExtra("selectedDayOfMonth", 0)
        val month = intent.getIntExtra("selectedMonth", 0)
        val year = intent.getIntExtra("selectedYear", 0)

        Log.i("##INFO", "year: $year, month: $month, day: $day, hour: $hour, minute: $minute");

        val calendar = Calendar.getInstance()
        calendar.set(year, month-1, day, hour, minute)
        return calendar.timeInMillis
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelID: String) {
        val name = "notif channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}