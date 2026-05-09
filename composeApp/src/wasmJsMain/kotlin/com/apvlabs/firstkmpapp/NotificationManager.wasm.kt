package com.apvlabs.firstkmpapp

actual suspend fun showNotificationPlatform(notification: NotificationData): Boolean {
    return false // Wasm implementation not yet available
}

actual suspend fun cancelNotificationPlatform(notificationId: String) {
    // Wasm implementation not yet available
}

actual fun areNotificationsEnabledPlatform(): Boolean {
    return false // Wasm implementation not yet available
}

actual suspend fun requestNotificationPermissionPlatform(): Boolean {
    return false // Wasm implementation not yet available
}
