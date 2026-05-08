package com.apvlabs.firstkmpapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

// Common notification data class
data class NotificationData(
    val id: String,
    val title: String,
    val message: String,
    val icon: String? = null,
    val sound: Boolean = true,
    val vibration: Boolean = true
)

// Common notification manager interface
object NotificationManager {
    
    // Initialize notification system
    fun initialize() {
        // Platform-specific initialization will be handled by expect/actual
    }
    
    // Show notification
    suspend fun showNotification(notification: NotificationData): Boolean {
        return try {
            showNotificationPlatform(notification)
        } catch (e: Exception) {
            println("Error showing notification: ${e.message}")
            false
        }
    }
    
    // Cancel notification
    suspend fun cancelNotification(notificationId: String) {
        try {
            cancelNotificationPlatform(notificationId)
        } catch (e: Exception) {
            println("Error canceling notification: ${e.message}")
        }
    }
    
    // Check if notifications are enabled
    fun areNotificationsEnabled(): Boolean {
        return areNotificationsEnabledPlatform()
    }
    
    // Request notification permission (for platforms that need it)
    suspend fun requestNotificationPermission(): Boolean {
        return try {
            requestNotificationPermissionPlatform()
        } catch (e: Exception) {
            println("Error requesting notification permission: ${e.message}")
            false
        }
    }
    
    // Convenience methods for common notification types
    
    suspend fun showTimeNotification(location: Location, currentTime: String) {
        val notification = NotificationData(
            id = "time_${location.city}_${Clock.System.now().toEpochMilliseconds()}",
            title = "World Clock - ${location.city}",
            message = "Current time: $currentTime",
            icon = CountryFlagService.getCountryFlag(location.country),
            sound = ClockManager.getPreferences().notificationSound,
            vibration = ClockManager.getPreferences().notificationVibration
        )
        showNotification(notification)
    }
    
    suspend fun showTimeDifferenceNotification(location: Location, difference: String) {
        val notification = NotificationData(
            id = "diff_${location.city}_${Clock.System.now().toEpochMilliseconds()}",
            title = "Time Difference - ${location.city}",
            message = "$difference from your local time",
            icon = CountryFlagService.getCountryFlag(location.country),
            sound = ClockManager.getPreferences().notificationSound,
            vibration = ClockManager.getPreferences().notificationVibration
        )
        showNotification(notification)
    }
    
    suspend fun showDayNightNotification(location: Location, isDayTime: Boolean) {
        val timeOfDay = if (isDayTime) "daytime" else "nighttime"
        val notification = NotificationData(
            id = "daynight_${location.city}_${Clock.System.now().toEpochMilliseconds()}",
            title = "Day/Night Update - ${location.city}",
            message = "It's currently $timeOfDay in ${location.city}",
            icon = CountryFlagService.getCountryFlag(location.country),
            sound = false, // Less intrusive
            vibration = false
        )
        showNotification(notification)
    }
}
