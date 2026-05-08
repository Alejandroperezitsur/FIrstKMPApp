package com.apvlabs.firstkmpapp

data class WorldClock(
    val id: String,
    val location: Location,
    val isFavorite: Boolean = false,
    val isLocalTime: Boolean = false,
    val order: Int = 0
)

data class UserPreferences(
    val is24HourFormat: Boolean = true,
    val showSeconds: Boolean = true,
    val showDate: Boolean = true,
    val showTimeDifference: Boolean = true,
    val autoRefresh: Boolean = true,
    val refreshInterval: Int = 1 // seconds
)

object ClockManager {
    private val _clocks = mutableListOf<WorldClock>()
    private var _preferences = UserPreferences()
    
    // Initialize with some default clocks
    init {
        initializeDefaultClocks()
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
    
    fun addClock(location: Location): WorldClock {
        val newClock = WorldClock(
            id = "clock_${kotlinx.datetime.Clock.System.now().epochSeconds}",
            location = location,
            order = _clocks.size
        )
        _clocks.add(newClock)
        return newClock
    }
    
    fun removeClock(clockId: String) {
        val iterator = _clocks.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().id == clockId) {
                iterator.remove()
            }
        }
        reorderClocks()
    }
    
    fun toggleFavorite(clockId: String) {
        for (i in _clocks.indices) {
            if (_clocks[i].id == clockId) {
                _clocks[i] = _clocks[i].copy(isFavorite = !_clocks[i].isFavorite)
                break
            }
        }
    }
    
    fun moveClock(fromIndex: Int, toIndex: Int) {
        if (fromIndex >= 0 && fromIndex < _clocks.size && toIndex >= 0 && toIndex < _clocks.size) {
            val clock = _clocks.removeAt(fromIndex)
            _clocks.add(toIndex, clock)
            reorderClocks()
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
    
    fun updatePreferences(newPreferences: UserPreferences) {
        _preferences = newPreferences
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
            if (clock.location.country.lowercase().contains(lowercaseQuery) ||
                clock.location.city.lowercase().contains(lowercaseQuery)) {
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
    
    fun clearAllClocks() {
        _clocks.clear()
        initializeDefaultClocks()
    }
}
