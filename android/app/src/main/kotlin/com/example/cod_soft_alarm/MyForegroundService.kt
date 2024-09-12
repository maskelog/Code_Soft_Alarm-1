package com.example.cod_soft_alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {
    private val CHANNEL_ID = "ForegroundServiceChannel"
    private val SNOOZE_REQUEST_CODE = 1
    private val STOP_REQUEST_CODE = 2

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 스누즈 버튼을 위한 Intent 및 PendingIntent 생성
        val snoozeIntent = Intent(this, MyForegroundService::class.java).apply {
            action = "SNOOZE_ALARM"
        }
        val snoozePendingIntent = PendingIntent.getService(this, SNOOZE_REQUEST_CODE, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 알람 종료 버튼을 위한 Intent 및 PendingIntent 생성
        val stopIntent = Intent(this, MyForegroundService::class.java).apply {
            action = "STOP_ALARM"
        }
        val stopPendingIntent = PendingIntent.getService(this, STOP_REQUEST_CODE, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 알림 빌드 (리소스 파일이 없는 경우 기본 아이콘 사용)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarm Service")
            .setContentText("The alarm is running in the background.")
            .setSmallIcon(R.mipmap.ic_launcher)  // 기본 아이콘 사용
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Snooze", snoozePendingIntent)  // 스누즈 버튼 추가
            .addAction(R.mipmap.ic_launcher, "Stop", stopPendingIntent)  // 알람 종료 버튼 추가
            .build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                "SNOOZE_ALARM" -> handleSnooze()  // 스누즈 처리
                "STOP_ALARM" -> handleStop()  // 알람 종료 처리
            }
        }
        return START_NOT_STICKY
    }

    // 스누즈 처리
    private fun handleSnooze() {
        // 스누즈 로직을 구현 (예: 5분 후 다시 알람 설정)
        val snoozeTimeMillis = System.currentTimeMillis() + 5 * 60 * 1000  // 5분 후
        val snoozeIntent = Intent(this, MyForegroundService::class.java)
        val pendingIntent = PendingIntent.getService(this, SNOOZE_REQUEST_CODE, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, snoozeTimeMillis, pendingIntent)

        stopSelf()  // 현재 알람을 종료
    }

    // 알람 종료 처리
    private fun handleStop() {
        stopSelf()  // Foreground Service 종료
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
