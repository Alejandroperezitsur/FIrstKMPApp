package com.apvlabs.firstkmpapp

import com.russhwolf.settings.JsonSettings
import com.russhwolf.settings.Settings
import java.io.File

actual fun createSettings(): Settings {
    val userHome = System.getProperty("user.home")
    val appDir = File(userHome, ".worldclock")
    appDir.mkdirs()
    val settingsFile = File(appDir, "settings.json")
    return JsonSettings(settingsFile)
}
