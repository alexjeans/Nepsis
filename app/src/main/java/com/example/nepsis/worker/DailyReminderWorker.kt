package com.example.nepsis.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nepsis.MainActivity
import com.example.nepsis.R
import com.example.nepsis.core.di.ServiceLocator
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

class DailyReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val sessionManager = ServiceLocator.provideSessionManager(context)
        val dao = ServiceLocator.provideDatabase(context).nepsisDao()

        val userId = sessionManager.getUserId()
        
        // Si no hay usuario logueado, no hacemos nada
        if (userId == null) return Result.success()

        // 1. Verificamos si ya hizo el Check-in hoy
        val today = LocalDate.now().toString()
        val moods = dao.getDailyMoodsByUserId(userId).firstOrNull() ?: emptyList()
        val hasCheckedInToday = moods.any { it.date == today }

        // 2. Si no lo ha hecho, lanzamos la notificación
        if (!hasCheckedInToday) {
            showNotification()
        }

        return Result.success()
    }

    private fun showNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "nepsis_daily_reminder"

        // Crear canal de notificaciones (Requerido para Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorio Diario",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Te recuerda hacer tu registro emocional diario"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent para abrir la app al tocar la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round) // Tu logo
            .setContentTitle("¿Cómo te sientes hoy?")
            .setContentText("No has hecho tu daily check-in. Tómate un minuto para ti. 💙")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}
