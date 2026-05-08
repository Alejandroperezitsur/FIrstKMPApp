package com.apvlabs.firstkmpapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable () -> Unit = {},
    active: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchHistory = ClockManager.getSearchHistory()
    val preferences = ClockManager.getPreferences()
    
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { searchQuery ->
            onSearch(searchQuery)
            keyboardController?.hide()
            onActiveChange(false)
        },
        onActiveChange = onActiveChange,
        modifier = modifier,
        placeholder = placeholder,
        active = active
    ) {
        if (query.isEmpty() && searchHistory.isNotEmpty()) {
            // Show search history
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Searches",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        if (preferences.rememberSearchHistory) {
                            TextButton(
                                onClick = {
                                    // Clear search history
                                    ClockManager.clearSearchHistory()
                                }
                            ) {
                                Text("Clear")
                            }
                        }
                    }
                }
                
                items(searchHistory) { historyItem ->
                    SearchHistoryItem(
                        query = historyItem,
                        onClick = {
                            onQueryChange(historyItem)
                            onSearch(historyItem)
                        },
                        onRemove = {
                            ClockManager.removeFromSearchHistory(historyItem)
                        }
                    )
                }
            }
        } else if (query.isNotEmpty()) {
            // Show search results
            val searchResults = ClockManager.searchClocks(query)
            
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (searchResults.isEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No results found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(searchResults) { clock ->
                        SearchResultItem(
                            clock = clock,
                            onClick = {
                                onQueryChange("")
                                onActiveChange(false)
                            }
                        )
                    }
                }
            }
        }
        
        // Show filter chips if query is empty
        if (query.isEmpty()) {
            FilterChipsSection(
                preferences = preferences,
                onPreferencesChanged = { newPrefs ->
                    // This would need to be handled by the parent component
                    // For now, we'll just update the ClockManager
                }
            )
        }
    }
}

@Composable
fun SearchHistoryItem(
    query: String,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = query,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove from history",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SearchResultItem(
    clock: WorldClock,
    onClick: () -> Unit
) {
    val isDayTime = TimeService.isDayTime(clock.location)
    val preferences = ClockManager.getPreferences()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                // Country flag (if enabled)
                if (preferences.showCountryFlags) {
                    Text(
                        text = CountryFlagService.getLocationIcon(clock.location),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                
                Column {
                    Text(
                        text = clock.location.city,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = clock.location.country,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Day/Night indicator (if enabled)
                if (preferences.showDayNightIndicator) {
                    Icon(
                        imageVector = if (isDayTime) Icons.Default.WbSunny else Icons.Default.NightsStay,
                        contentDescription = null,
                        tint = if (isDayTime) Color(0xFFFF9800) else Color(0xFF2196F3),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                // Timezone
                Text(
                    text = TimeService.getTimeZoneString(clock.location),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun FilterChipsSection(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Quick Filters",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                selected = preferences.searchFilterFavorites,
                onClick = { onPreferencesChanged(preferences.copy(searchFilterFavorites = !preferences.searchFilterFavorites)) },
                label = { Text("Favorites") },
                leadingIcon = if (preferences.searchFilterFavorites) {
                    { Icon(Icons.Default.Favorite, contentDescription = null) }
                } else null
            )
            
            FilterChip(
                selected = preferences.searchFilterCustom,
                onClick = { onPreferencesChanged(preferences.copy(searchFilterCustom = !preferences.searchFilterCustom)) },
                label = { Text("Custom") },
                leadingIcon = if (preferences.searchFilterCustom) {
                    { Icon(Icons.Default.Add, contentDescription = null) }
                } else null
            )
            
            FilterChip(
                selected = preferences.searchFilterPopular,
                onClick = { onPreferencesChanged(preferences.copy(searchFilterPopular = !preferences.searchFilterPopular)) },
                label = { Text("Popular") },
                leadingIcon = if (preferences.searchFilterPopular) {
                    { Icon(Icons.Default.Star, contentDescription = null) }
                } else null
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Geographic filters
        Text(
            text = "Geographic Filters",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                selected = false, // This would need to be added to preferences
                onClick = { /* Handle continent filter */ },
                label = { Text("Americas") }
            )
            
            FilterChip(
                selected = false,
                onClick = { /* Handle continent filter */ },
                label = { Text("Europe") }
            )
            
            FilterChip(
                selected = false,
                onClick = { /* Handle continent filter */ },
                label = { Text("Asia") }
            )
            
            FilterChip(
                selected = false,
                onClick = { /* Handle continent filter */ },
                label = { Text("All") }
            )
        }
    }
}
