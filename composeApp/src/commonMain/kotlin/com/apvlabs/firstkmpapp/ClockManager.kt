package com.apvlabs.firstkmpapp

data class WorldClock(
    val id: String,
    val location: Location,
    val isFavorite: Boolean = false,
    val isLocalTime: Boolean = false,
    val order: Int = 0
)

data class UserPreferences(
    // Display Settings
    val is24HourFormat: Boolean = true,
    val showSeconds: Boolean = true,
    val showDate: Boolean = true,
    val showTimeDifference: Boolean = true,
    val dateFormat: String = "MMM dd, yyyy", // Format pattern
    val clockCardStyle: String = "standard", // standard, compact, detailed
    val showCountryFlags: Boolean = true,
    val showDayNightIndicator: Boolean = true,
    
    // Behavior Settings
    val autoRefresh: Boolean = true,
    val refreshInterval: Int = 1, // seconds
    val defaultClocksOnStartup: Boolean = true,
    val rememberSearchHistory: Boolean = true,
    val maxSearchHistoryItems: Int = 10,
    
    // Theme Settings
    val themeMode: String = "auto", // light, dark, auto
    val dynamicColors: Boolean = true, // Material You on Android
    
    // Notification Settings
    val enableNotifications: Boolean = false,
    val notificationSound: Boolean = true,
    val notificationVibration: Boolean = true, // Android only
    
    // Storage Settings
    val autoBackup: Boolean = false,
    val backupFrequency: String = "daily", // daily, weekly, monthly
    
    // Search Settings
    val searchFilterFavorites: Boolean = false,
    val searchFilterCustom: Boolean = false,
    val searchFilterPopular: Boolean = true
)

object ClockManager {
    private val _clocks = mutableListOf<WorldClock>()
    private var _preferences = UserPreferences()
    private var _searchHistory = mutableListOf<String>()
    
    // Initialize with some default clocks
    init {
        initializeDefaultClocks()
    }
    
    // Initialize storage and load saved data
    suspend fun initializeStorage() {
        try {
            StorageManager.initialize(createSettings())
            
            // Load user preferences
            val savedPreferences = StorageManager.loadUserPreferences()
            _preferences = savedPreferences
            
            // Load world clocks
            val savedClocks = StorageManager.loadWorldClocks()
            if (savedClocks.isNotEmpty()) {
                _clocks.clear()
                _clocks.addAll(savedClocks)
            } else if (_preferences.defaultClocksOnStartup) {
                initializeDefaultClocks()
            }
            
            // Load search history
            val savedHistory = StorageManager.loadSearchHistory()
            _searchHistory.clear()
            _searchHistory.addAll(savedHistory)
            
        } catch (e: Exception) {
            println("Error initializing storage: ${e.message}")
            initializeDefaultClocks()
        }
    }
    
    private fun initializeDefaultClocks() {
        val popularLocations = LocationData.getPopularLocations()
        val locationsToAdd = if (popularLocations.size >= 4) {
            popularLocations.subList(0, 4)
        } else {
            popularLocations
        }
        
        for ((index, location) in locationsToAdd.withIndex()) {
            _clocks.add(
                WorldClock(
                    id = "default_${index}",
                    location = location,
                    isFavorite = true,
                    order = index
                )
            )
        }
        
        // Add local time as first clock
        try {
            val localTimeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
            val localLocation = Location(
                country = "Local",
                city = "Current Time",
                timezoneId = localTimeZone.id,
                utcOffsetHours = 0
            )
            _clocks.add(
                0,
                WorldClock(
                    id = "local_time",
                    location = localLocation,
                    isLocalTime = true,
                    order = -1
                )
            )
        } catch (e: Exception) {
            // Fallback if we can't get local timezone
            val fallbackLocation = Location(
                country = "Local",
                city = "Current Time",
                timezoneId = "UTC",
                utcOffsetHours = 0
            )
            _clocks.add(
                0,
                WorldClock(
                    id = "local_time",
                    location = fallbackLocation,
                    isLocalTime = true,
                    order = -1
                )
            )
        }
    }
    
    fun getClocks(): List<WorldClock> {
        return _clocks.sortedBy { clock: WorldClock -> clock.order }
    }
    
    suspend fun addClock(location: Location): WorldClock {
        val newClock = WorldClock(
            id = "clock_${kotlinx.datetime.Clock.System.now().epochSeconds}",
            location = location,
            order = _clocks.size
        )
        _clocks.add(newClock)
        StorageManager.saveWorldClocks(_clocks)
        return newClock
    }
    
    suspend fun removeClock(clockId: String) {
        val iterator = _clocks.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().id == clockId) {
                iterator.remove()
            }
        }
        reorderClocks()
        StorageManager.saveWorldClocks(_clocks)
    }
    
    suspend fun toggleFavorite(clockId: String) {
        for (i in _clocks.indices) {
            if (_clocks[i].id == clockId) {
                _clocks[i] = _clocks[i].copy(isFavorite = !_clocks[i].isFavorite)
                break
            }
        }
        StorageManager.saveWorldClocks(_clocks)
    }
    
    suspend fun moveClock(fromIndex: Int, toIndex: Int) {
        if (fromIndex >= 0 && fromIndex < _clocks.size && toIndex >= 0 && toIndex < _clocks.size) {
            val clock = _clocks.removeAt(fromIndex)
            _clocks.add(toIndex, clock)
            reorderClocks()
            StorageManager.saveWorldClocks(_clocks)
        }
    }
    
    private fun reorderClocks() {
        for (i in _clocks.indices) {
            if (!_clocks[i].isLocalTime) {
                _clocks[i] = _clocks[i].copy(order = i)
            }
        }
    }
    
    fun getPreferences(): UserPreferences {
        return _preferences
    }
    
    suspend fun updatePreferences(newPreferences: UserPreferences) {
        _preferences = newPreferences
        StorageManager.saveUserPreferences(newPreferences)
    }
    
    fun toggleTimeFormat() {
        _preferences = _preferences.copy(is24HourFormat = !_preferences.is24HourFormat)
    }
    
    fun toggleShowSeconds() {
        _preferences = _preferences.copy(showSeconds = !_preferences.showSeconds)
    }
    
    fun toggleShowDate() {
        _preferences = _preferences.copy(showDate = !_preferences.showDate)
    }
    
    fun toggleShowTimeDifference() {
        _preferences = _preferences.copy(showTimeDifference = !_preferences.showTimeDifference)
    }
    
    fun toggleAutoRefresh() {
        _preferences = _preferences.copy(autoRefresh = !_preferences.autoRefresh)
    }
    
    fun getFavoriteClocks(): List<WorldClock> {
        val favoriteClocks = mutableListOf<WorldClock>()
        for (clock in _clocks) {
            if (clock.isFavorite) {
                favoriteClocks.add(clock)
            }
        }
        return favoriteClocks.sortedBy { clock: WorldClock -> clock.order }
    }
    
    fun searchClocks(query: String): List<WorldClock> {
        if (query.isEmpty()) return emptyList()
        
        val lowercaseQuery = query.lowercase()
        val results = mutableListOf<WorldClock>()
        
        for (clock in _clocks) {
            // Apply text search filter
            val matchesText = clock.location.country.lowercase().contains(lowercaseQuery) ||
                            clock.location.city.lowercase().contains(lowercaseQuery)
            
            if (!matchesText) continue
            
            // Apply preference-based filters
            var passesFilters = true
            
            // Filter by favorites
            if (_preferences.searchFilterFavorites && !clock.isFavorite) {
                passesFilters = false
            }
            
            // Filter by custom clocks (non-local, non-default)
            if (_preferences.searchFilterCustom) {
                val isDefault = LocationData.getPopularLocations().any { 
                    it.country == clock.location.country && it.city == clock.location.city 
                }
                if (isDefault || clock.isLocalTime) {
                    passesFilters = false
                }
            }
            
            // Filter by popular locations
            if (_preferences.searchFilterPopular) {
                val isPopular = LocationData.getPopularLocations().any { 
                    it.country == clock.location.country && it.city == clock.location.city 
                }
                if (!isPopular) {
                    passesFilters = false
                }
            }
            
            if (passesFilters) {
                results.add(clock)
            }
        }
        
        return results
    }
    
    fun getClockById(clockId: String): WorldClock? {
        for (clock in _clocks) {
            if (clock.id == clockId) {
                return clock
            }
        }
        return null
    }
    
    suspend fun clearAllClocks() {
        _clocks.clear()
        initializeDefaultClocks()
        StorageManager.saveWorldClocks(_clocks)
    }
    
    // Search History Management
    fun getSearchHistory(): List<String> {
        return _searchHistory.toList()
    }
    
    suspend fun addToSearchHistory(query: String) {
        if (!_preferences.rememberSearchHistory || query.isBlank()) return
        
        // Remove if already exists
        _searchHistory.remove(query)
        
        // Add to front
        _searchHistory.add(0, query)
        
        // Limit to max items
        while (_searchHistory.size > _preferences.maxSearchHistoryItems) {
            _searchHistory.removeAt(_searchHistory.size - 1)
        }
        
        StorageManager.saveSearchHistory(_searchHistory)
    }
    
    suspend fun clearSearchHistory() {
        _searchHistory.clear()
        StorageManager.saveSearchHistory(_searchHistory)
    }
    
    suspend fun removeFromSearchHistory(query: String) {
        _searchHistory.remove(query)
        StorageManager.saveSearchHistory(_searchHistory)
    }
    
    // Data Export/Import
    suspend fun exportAllData(): String {
        return StorageManager.exportData()
    }
    
    suspend fun importAllData(dataJson: String): Boolean {
        val success = StorageManager.importData(dataJson)
        if (success) {
            // Reload data after import
            _preferences = StorageManager.loadUserPreferences()
            val savedClocks = StorageManager.loadWorldClocks()
            _clocks.clear()
            _clocks.addAll(savedClocks)
            val savedHistory = StorageManager.loadSearchHistory()
            _searchHistory.clear()
            _searchHistory.addAll(savedHistory)
        }
        return success
    }
    
    suspend fun clearAllData() {
        _clocks.clear()
        _searchHistory.clear()
        _preferences = UserPreferences()
        initializeDefaultClocks()
        StorageManager.clearAllData()
    }
}
