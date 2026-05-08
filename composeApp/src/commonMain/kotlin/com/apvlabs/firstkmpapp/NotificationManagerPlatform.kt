package com.apvlabs.firstkmpapp

// Platform-specific implementation methods
expect suspend fun showNotificationPlatform(notification: NotificationData): Boolean
expect suspend fun cancelNotificationPlatform(notificationId: String)
expect fun areNotificationsEnabledPlatform(): Boolean
expect suspend fun requestNotificationPermissionPlatform(): Boolean
