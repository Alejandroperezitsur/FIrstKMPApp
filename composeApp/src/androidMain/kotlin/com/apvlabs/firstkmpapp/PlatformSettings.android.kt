package com.apvlabs.firstkmpapp

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.Settings

actual fun createSettings(): Settings {
    // Get the application context - this will be provided by the Android app
    val context = getContext()
    return SharedPreferencesSettings(context.getSharedPreferences("world_clock_prefs", Context.MODE_PRIVATE))
}

private var applicationContext: Context? = null

fun setApplicationContext(context: Context) {
    applicationContext = context
}

fun getContext(): Context {
    return applicationContext ?: throw IllegalStateException("Application context not set. Call setApplicationContext() first.")
}
