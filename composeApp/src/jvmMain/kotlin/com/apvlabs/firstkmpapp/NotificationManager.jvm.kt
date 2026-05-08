package com.apvlabs.firstkmpapp

import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.SystemTray
import java.awt.Image
import java.awt.MediaTracker
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream
import java.util.Base64

// Desktop (JVM) implementation using System Tray notifications
private suspend fun NotificationManager.showNotificationPlatform(notification: NotificationData): Boolean {
    return try {
        if (!SystemTray.isSupported()) {
            println("System tray not supported on this platform")
            return false
        }
        
        val systemTray = SystemTray.getSystemTray()
        
        // Create a simple tray icon (using default system icon)
        val trayIcon = TrayIcon(
            createTrayIconImage(),
            "World Clock"
        )
        
        trayIcon.isImageAutoSize = true
        trayIcon.toolTip = "World Clock"
        
        // Add to system tray
        systemTray.add(trayIcon)
        
        // Show notification
        val displayMessage = if (notification.icon != null) {
            "${notification.icon} ${notification.message}"
        } else {
            notification.message
        }
        
        trayIcon.displayMessage(
            notification.title,
            displayMessage,
            TrayIcon.MessageType.INFO
        )
        
        // Remove tray icon after a delay
        Thread {
            Thread.sleep(5000) // Show for 5 seconds
            systemTray.remove(trayIcon)
        }.start()
        
        true
    } catch (e: Exception) {
        println("Error showing desktop notification: ${e.message}")
        false
    }
}

private suspend fun NotificationManager.cancelNotificationPlatform(notificationId: String) {
    // Desktop notifications using system tray don't support canceling individual notifications
    // They auto-dismiss after a short time
    try {
        // Could implement a tracking system if needed
    } catch (e: Exception) {
        println("Error canceling desktop notification: ${e.message}")
    }
}

private fun NotificationManager.areNotificationsEnabledPlatform(): Boolean {
    return try {
        SystemTray.isSupported()
    } catch (e: Exception) {
        println("Error checking desktop notification support: ${e.message}")
        false
    }
}

private suspend fun NotificationManager.requestNotificationPermissionPlatform(): Boolean {
    // Desktop platforms generally don't require explicit permission for system tray notifications
    // But some systems might have restrictions
    return areNotificationsEnabledPlatform()
}

private fun createTrayIconImage(): Image {
    return try {
        // Create a simple 16x16 image using AWT
        val image = java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()
        
        // Draw a simple clock icon
        graphics.color = java.awt.Color.BLUE
        graphics.fillOval(2, 2, 12, 12)
        graphics.color = java.awt.Color.WHITE
        graphics.drawLine(8, 8, 8, 4) // Hour hand
        graphics.drawLine(8, 8, 10, 8) // Minute hand
        graphics.dispose()
        
        image
    } catch (e: Exception) {
        println("Error creating tray icon image: ${e.message}")
        // Fallback to system default
        Toolkit.getDefaultToolkit().createImage(byteArrayOf())
    }
}
