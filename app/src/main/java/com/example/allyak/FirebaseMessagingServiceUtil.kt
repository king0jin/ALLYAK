package com.example.allyak

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingServiceUtil : FirebaseMessagingService() {
    /**
     * 메시지가 왔을때 onMessageReceived 에서 받음.
     * @param message
     */
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification != null) {
            val intent = Intent()
            intent.action = "com.package.notification"
            sendBroadcast(intent)
        }
        sendNotfication(message)
    }

    /**
     *
     * fcm 알림이 왔을때 벨소리,아이콘,등을 설정할 수 있음.
     * @param remoteMessage
     */
    private fun sendNotfication(remoteMessage: RemoteMessage) {
        //RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시되도록
        val uniId = (Math.random() * 100).toInt()

        // 일회용 PendingIntent
        // PendingIntent : Intent의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack 을 경로만 남긴다. A-B-C-D-B => A-B
        val pendingIntent =
            PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_IMMUTABLE)
        val name = (Math.random() * 100).toInt()
        // 알림 채널 이름
        val channelId = "postman$name"
        Log.i("##INFO", "sendNotification(): channelId = $channelId")

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정한다.
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // 아이콘 설정
                .setContentTitle(remoteMessage.messageType) // 타입
                .setContentTitle(remoteMessage.notification!!.title) // 제목
                .setContentText(remoteMessage.notification!!.body) // 메시지 내용
                .setAutoCancel(true)
                .setSound(soundUri) // 알림 소리
                .setContentIntent(pendingIntent) // 알림 실행 시 Intent
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var channel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        //알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }
}