package com.apvlabs.firstkmpapp

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.russhwolf.settings.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object StorageManager {
    private lateinit var settings: Settings
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    // Storage keys
    private const val USER_PREFERENCES_KEY = "user_preferences"
    private const val WORLD_CLOCKS_KEY = "world_clocks"
    private const val SEARCH_HISTORY_KEY = "search_history"
    private const val APP_VERSION_KEY = "app_version"
    private const val FIRST_LAUNCH_KEY = "first_launch"
    
    fun initialize(settings: Settings) {
        this.settings = settings
    }
    
    // User Preferences Storage
    suspend fun saveUserPreferences(preferences: UserPreferences) = withContext(Dispatchers.Default) {
        try {
            val preferencesJson = json.encodeToString(preferences)
            settings[USER_PREFERENCES_KEY] = preferencesJson
        } catch (e: Exception) {
            println("Error saving user preferences: ${e.message}")
        }
    }
    
    suspend fun loadUserPreferences(): UserPreferences = withContext(Dispatchers.Default) {
        try {
            val preferencesJson = settings.getString(USER_PREFERENCES_KEY, "")
            if (preferencesJson.isNotEmpty()) {
                json.decodeFromString<UserPreferences>(preferencesJson)
            } else {
                UserPreferences() // Return default preferences
            }
        } catch (e: Exception) {
            println("Error loading user preferences: ${e.message}")
            UserPreferences() // Return default preferences on error
        }
    }
    
    // World Clocks Storage
    suspend fun saveWorldClocks(clocks: List<WorldClock>) = withContext(Dispatchers.Default) {
        try {
            val clocksJson = json.encodeToString(clocks)
            settings[WORLD_CLOCKS_KEY] = clocksJson
        } catch (e: Exception) {
            println("Error saving world clocks: ${e.message}")
        }
    }
    
    suspend fun loadWorldClocks(): List<WorldClock> = withContext(Dispatchers.Default) {
        try {
            val clocksJson = settings.getString(WORLD_CLOCKS_KEY, "")
            if (clocksJson.isNotEmpty()) {
                json.decodeFromString<List<WorldClock>>(clocksJson)
            } else {
                emptyList() // Return empty list if no saved clocks
            }
        } catch (e: Exception) {
            println("Error loading world clocks: ${e.message}")
            emptyList() // Return empty list on error
        }
    }
    
    // Search History Storage
    suspend fun saveSearchHistory(searchHistory: List<String>) = withContext(Dispatchers.Default) {
        try {
            val historyJson = json.encodeToString(searchHistory)
            settings[SEARCH_HISTORY_KEY] = historyJson
        } catch (e: Exception) {
            println("Error saving search history: ${e.message}")
        }
    }
    
    suspend fun loadSearchHistory(): List<String> = withContext(Dispatchers.Default) {
        try {
            val historyJson = settings.getString(SEARCH_HISTORY_KEY, "")
            if (historyJson.isNotEmpty()) {
                json.decodeFromString<List<String>>(historyJson)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error loading search history: ${e.message}")
            emptyList()
        }
    }
    
    // App State Storage
    suspend fun saveAppVersion(version: String) = withContext(Dispatchers.Default) {
        try {
            settings[APP_VERSION_KEY] = version
        } catch (e: Exception) {
            println("Error saving app version: ${e.message}")
        }
    }
    
    fun getAppVersion(): String {
        return try {
            settings.getString(APP_VERSION_KEY, "1.0.0")
        } catch (e: Exception) {
            "1.0.0"
        }
    }
    
    suspend fun setFirstLaunchCompleted() = withContext(Dispatchers.Default) {
        try {
            settings[FIRST_LAUNCH_KEY] = false
        } catch (e: Exception) {
            println("Error setting first launch: ${e.message}")
        }
    }
    
    fun isFirstLaunch(): Boolean {
        return try {
            settings.getBoolean(FIRST_LAUNCH_KEY, true)
        } catch (e: Exception) {
            true
        }
    }
    
    // Utility Functions
    suspend fun clearAllData() = withContext(Dispatchers.Default) {
        try {
            settings.clear()
        } catch (e: Exception) {
            println("Error clearing all data: ${e.message}")
        }
    }
    
    suspend fun exportData(): String = withContext(Dispatchers.Default) {
        try {
            val exportData = mapOf(
                "userPreferences" to json.encodeToString(loadUserPreferences()),
                "worldClocks" to json.encodeToString(loadWorldClocks()),
                "searchHistory" to json.encodeToString(loadSearchHistory()),
                "appVersion" to getAppVersion(),
                "exportTimestamp" to kotlinx.datetime.Clock.System.now().toString()
            )
            json.encodeToString(exportData)
        } catch (e: Exception) {
            println("Error exporting data: ${e.message}")
            ""
        }
    }
    
    suspend fun importData(dataJson: String): Boolean = withContext(Dispatchers.Default) {
        try {
            val importData = json.decodeFromString<Map<String, String>>(dataJson)
            
            // Import user preferences
            importData["userPreferences"]?.let { prefsJson ->
                val preferences = json.decodeFromString<UserPreferences>(prefsJson)
                saveUserPreferences(preferences)
            }
            
            // Import world clocks
            importData["worldClocks"]?.let { clocksJson ->
                val clocks = json.decodeFromString<List<WorldClock>>(clocksJson)
                saveWorldClocks(clocks)
            }
            
            // Import search history
            importData["searchHistory"]?.let { historyJson ->
                val history = json.decodeFromString<List<String>>(historyJson)
                saveSearchHistory(history)
            }
            
            true
        } catch (e: Exception) {
            println("Error importing data: ${e.message}")
            false
        }
    }
}
