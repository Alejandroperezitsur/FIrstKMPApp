package com.apvlabs.firstkmpapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

private lateinit var androidContext: Context
private const val CHANNEL_ID = "world_clock_channel"
private const val CHANNEL_NAME = "World Clock Notifications"

fun setNotificationContext(context: Context) {
    androidContext = context
}

// Android-specific implementation
private suspend fun NotificationManager.showNotificationPlatform(notification: NotificationData): Boolean {
    return try {
        if (!::androidContext.isInitialized) {
            println("Android context not initialized")
            return false
        }
        
        val notificationManager = NotificationManagerCompat.from(androidContext)
        
        // Check if notifications are enabled
        if (!notificationManager.areNotificationsEnabled()) {
            return false
        }
        
        // Create notification channel (required for Android 8.0+)
        createNotificationChannel()
        
        // Create intent for opening the app when notification is tapped
        val intent = androidContext.packageManager.getLaunchIntentForPackage(androidContext.packageName)
        val pendingIntent = PendingIntent.getActivity(
            androidContext, 
            0, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Build notification
        val builder = NotificationCompat.Builder(androidContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Use system icon for now
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        
        // Add sound if enabled
        if (notification.sound) {
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND)
        }
        
        // Add vibration if enabled
        if (notification.vibration) {
            builder.setDefaults(builder.mNotification.defaults or NotificationCompat.DEFAULT_VIBRATE)
        }
        
        // Show notification
        notificationManager.notify(notification.id.hashCode(), builder.build())
        true
    } catch (e: SecurityException) {
        println("Permission denied for notifications: ${e.message}")
        false
    } catch (e: Exception) {
        println("Error showing Android notification: ${e.message}")
        false
    }
}

private suspend fun NotificationManager.cancelNotificationPlatform(notificationId: String) {
    try {
        if (!::androidContext.isInitialized) return
        
        val notificationManager = NotificationManagerCompat.from(androidContext)
        notificationManager.cancel(notificationId.hashCode())
    } catch (e: Exception) {
        println("Error canceling Android notification: ${e.message}")
    }
}

private fun NotificationManager.areNotificationsEnabledPlatform(): Boolean {
    return try {
        if (!::androidContext.isInitialized) return false
        
        val notificationManager = NotificationManagerCompat.from(androidContext)
        notificationManager.areNotificationsEnabled()
    } catch (e: Exception) {
        println("Error checking notification permissions: ${e.message}")
        false
    }
}

private suspend fun NotificationManager.requestNotificationPermissionPlatform(): Boolean {
    return try {
        if (!::androidContext.isInitialized) return false
        
        // For Android 13+ (API 33+), we need to request POST_NOTIFICATIONS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                androidContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        
        // For older versions, notifications are generally enabled by default
        true
    } catch (e: Exception) {
        println("Error requesting notification permission: ${e.message}")
        false
    }
}

private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for World Clock app"
            enableLights(true)
            enableVibration(true)
        }
        
        val notificationManager = androidContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
