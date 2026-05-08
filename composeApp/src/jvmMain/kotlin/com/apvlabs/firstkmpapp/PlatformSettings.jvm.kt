package com.apvlabs.firstkmpapp

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

actual fun createSettings(): Settings {
    val preferences = Preferences.userRoot().node("com.apvlabs.firstkmpapp")
    return PreferencesSettings(preferences)
}
