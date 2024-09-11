package com.example.cod_soft_alarm

import android.content.Intent
import android.os.Build
import io.flutter.embedding.android.FlutterActivity
import android.os.Bundle
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    // MethodChannel의 이름을 정의
    private val CHANNEL = "com.example.cod_soft_alarm/foregroundService"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MethodChannel을 통해 Flutter와 네이티브(Android) 간 통신을 설정
        MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "startForegroundService" -> {
                    startForegroundService() // Foreground Service 시작
                    result.success(null)
                }
                "stopForegroundService" -> {
                    stopForegroundService() // Foreground Service 중지
                    result.success(null)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    // Foreground Service 시작
    private fun startForegroundService() {
        val intent = Intent(this, MyForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent) // Android O 이상에서 사용
        } else {
            startService(intent) // 구버전에서 사용
        }
    }

    // Foreground Service 중지
    private fun stopForegroundService() {
        val intent = Intent(this, MyForegroundService::class.java)
        stopService(intent)
    }
}
