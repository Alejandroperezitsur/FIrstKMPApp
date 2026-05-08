package com.apvlabs.firstkmpapp

// Wasm implementation using Web Notifications API
actual suspend fun showNotificationPlatform(notification: NotificationData): Boolean {
    // Basic placeholder for Wasm notifications
    println("Showing Wasm notification: ${notification.title} - ${notification.message}")
    return true
}

actual suspend fun cancelNotificationPlatform(notificationId: String) {
    println("Canceling Wasm notification: $notificationId")
}

actual fun areNotificationsEnabledPlatform(): Boolean {
    return true
}

actual suspend fun requestNotificationPermissionPlatform(): Boolean {
    return true
}
