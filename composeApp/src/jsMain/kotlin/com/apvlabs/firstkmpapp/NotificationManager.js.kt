package com.apvlabs.firstkmpapp

// Web implementation using Web Notifications API with basic JS interop
actual suspend fun showNotificationPlatform(notification: NotificationData): Boolean {
    return try {
        // Check if notifications are supported
        if (!isNotificationSupported()) {
            println("Web notifications not supported")
            return false
        }
        
        // Check if we have permission
        if (!areNotificationsEnabledPlatform()) {
            println("Notification permission not granted")
            return false
        }
        
        // Create notification using basic JS interop
        createWebNotification(notification.title, notification.message, notification.id)
        
        true
    } catch (e: Exception) {
        println("Error showing web notification: ${e.message}")
        false
    }
}

actual suspend fun cancelNotificationPlatform(notificationId: String) {
    try {
        // Web notifications don't have a direct cancel-by-tag method
        // We could track active notifications and close them
        // For now, this is a no-op as web notifications auto-close
        println("Canceling web notification: $notificationId")
    } catch (e: Exception) {
        println("Error canceling web notification: ${e.message}")
    }
}

actual fun areNotificationsEnabledPlatform(): Boolean {
    return try {
        if (!isNotificationSupported()) return false
        
        // Check notification permission using basic JS interop
        checkNotificationPermission()
    } catch (e: Exception) {
        println("Error checking web notification permission: ${e.message}")
        false
    }
}

actual suspend fun requestNotificationPermissionPlatform(): Boolean {
    return try {
        if (!isNotificationSupported()) return false
        
        // Request permission using basic JS interop
        requestWebNotificationPermission()
    } catch (e: Exception) {
        println("Error requesting web notification permission: ${e.message}")
        false
    }
}

private fun isNotificationSupported(): Boolean {
    return try {
        // Check if Notification API is available using basic JS interop
        checkNotificationSupport()
    } catch (e: Exception) {
        false
    }
}

// Basic JS interop functions
private fun createWebNotification(title: String, body: String, tag: String) {
    // Use dynamic JS evaluation to create notification
    js("""
        if (typeof window !== 'undefined' && 'Notification' in window) {
            var notification = new Notification(title, {
                body: body,
                tag: tag,
                requireInteraction: false
            });
            
            setTimeout(function() {
                notification.close();
            }, 5000);
        }
    """)
}

private fun checkNotificationPermission(): Boolean {
    return try {
        js("""
            (typeof window !== 'undefined' && 'Notification' in window && Notification.permission === 'granted')
        """) as Boolean
    } catch (e: Exception) {
        false
    }
}

private fun requestWebNotificationPermission(): Boolean {
    return try {
        js("""
            (function() {
                if (typeof window !== 'undefined' && 'Notification' in window) {
                    if (Notification.permission === 'granted') {
                        return true;
                    } else if (Notification.permission !== 'denied') {
                        Notification.requestPermission().then(function(permission) {
                            return permission === 'granted';
                        });
                    }
                }
                return false;
            })()
        """) as Boolean
    } catch (e: Exception) {
        false
    }
}

private fun checkNotificationSupport(): Boolean {
    return try {
        js("""
            (typeof window !== 'undefined' && 'Notification' in window)
        """) as Boolean
    } catch (e: Exception) {
        false
    }
}
