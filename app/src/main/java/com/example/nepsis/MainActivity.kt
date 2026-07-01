package com.example.nepsis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nepsis.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // --- INICIAR PROGRAMACIÓN DE NOTIFICACIONES ---
        scheduleDailyReminder()
        // ----------------------------------------------

        setContent {
            NepsisApp()
        }
    }

    private fun scheduleDailyReminder() {
        // Crea una tarea que se repetirá cada 24 horas
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            24, TimeUnit.HOURS
        ).build()

        // Le dice a Android que la mantenga en cola, y que si ya existe (KEEP), no la duplique
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "DailyCheckInReminder",
            ExistingPeriodicWorkPolicy.KEEP, 
            dailyWorkRequest
        )
    }
}
