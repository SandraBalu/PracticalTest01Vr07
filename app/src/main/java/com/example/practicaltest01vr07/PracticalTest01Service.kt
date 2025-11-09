
package com.example.practicaltest01vr07

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class PracticalTest01Service : Service() {

    // Thread-ul care rulează în fundal și generează numere
    private var processingThread: ProcessingThread? = null

    override fun onCreate() {
        super.onCreate()

        // ID-ul canalului de notificare pentru foreground service
        val CHANNEL_ID = "Colocviu"

        // 1. Creăm canalul de notificare (obligatoriu Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Colocviu EIM Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // 2. Construim notificarea minimală
        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Colocviu EIM")
                .setContentText("Service-ul rulează și generează numere...")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build()

        // 3. Pornim serviciul în foreground (altfel moare pe versiunile noi)
        startForeground(1, notification)
    }

    // Este apelată de fiecare dată când cineva pornește serviciul cu startService(...)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Pornește thread-ul doar dacă nu e deja pornit
        if (processingThread == null || !processingThread!!.isAlive) {
            processingThread = ProcessingThread(applicationContext)
            processingThread!!.start()
        }

        // Serviciul va fi repornit cu același intent dacă sistemul îl omoară
        return START_REDELIVER_INTENT
    }

    // Nu folosim bound service, deci return null
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        // Când serviciul se oprește, oprim elegant thread-ul
        processingThread?.stopThread()
        super.onDestroy()
    }
}
