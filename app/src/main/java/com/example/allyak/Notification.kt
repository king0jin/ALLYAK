package com.example.allyak

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

var notificationID = 1

const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val channelID = intent?.getStringExtra("channelID") ?: "channel1" // 기본값은 "channel1"
        val notificationID = intent?.getStringExtra("notificationID")

        val notification = NotificationCompat.Builder(context!!, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("알약 복용 알림")
            .setContentText(
                intent!!.getStringExtra(titleExtra) + "을/를 복용할 시간입니다."
            )
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID!!.toInt(), notification)

        Log.d("Notification", notificationID)
    }
}