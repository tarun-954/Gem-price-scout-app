package com.example.myapplication


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class PriceCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val gemName = inputData.getString("gem_name") ?: return Result.failure()
        val currentPrice = fetchGemPriceFromAPI(gemName) // Simulate or call actual API

        val userThreshold = UserPreferences.getAlertPrice(applicationContext, gemName)

        if (currentPrice < userThreshold) {
            sendNotification(gemName, currentPrice)
        }

        return Result.success()
    }

    private fun fetchGemPriceFromAPI(gemName: String): Float {
        return (50..100).random().toFloat() // Simulate random price
    }

    private fun sendNotification(gemName: String, price: Float) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "gem_alerts"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Gem Alerts", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Price Drop Alert!")
            .setContentText("Price of $gemName is now ₹$price")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(gemName.hashCode(), notification)
    }
}
