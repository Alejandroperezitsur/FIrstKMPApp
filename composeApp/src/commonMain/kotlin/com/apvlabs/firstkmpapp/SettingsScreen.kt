package com.apvlabs.firstkmpapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onPreferencesUpdated: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var preferences by remember { mutableStateOf(ClockManager.getPreferences()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display Settings Section
            item {
                SettingsSection(title = "Display Settings", icon = Icons.Default.Palette) {
                    DisplaySettingsContent(
                        preferences = preferences,
                        onPreferencesChanged = { newPrefs ->
                            preferences = newPrefs
                        }
                    )
                }
            }
            
            // Behavior Settings Section
            item {
                SettingsSection(title = "Behavior Settings", icon = Icons.Default.Settings) {
                    BehaviorSettingsContent(
                        preferences = preferences,
                        onPreferencesChanged = { newPrefs ->
                            preferences = newPrefs
                        }
                    )
                }
            }
            
            // Theme Settings Section
            item {
                SettingsSection(title = "Theme Settings", icon = Icons.Default.DarkMode) {
                    ThemeSettingsContent(
                        preferences = preferences,
                        onPreferencesChanged = { newPrefs ->
                            preferences = newPrefs
                        }
                    )
                }
            }
            
            // Notification Settings Section
            item {
                SettingsSection(title = "Notification Settings", icon = Icons.Default.Notifications) {
                    NotificationSettingsContent(
                        preferences = preferences,
                        onPreferencesChanged = { newPrefs ->
                            preferences = newPrefs
                        }
                    )
                }
            }
            
            // Storage Settings Section
            item {
                SettingsSection(title = "Storage Settings", icon = Icons.Default.Storage) {
                    StorageSettingsContent(
                        preferences = preferences,
                        onPreferencesChanged = { newPrefs ->
                            preferences = newPrefs
                        }
                    )
                }
            }
            
            // Search Settings Section
            item {
                SettingsSection(title = "Search Settings", icon = Icons.Default.Search) {
                    SearchSettingsContent(
                        preferences = preferences,
                        onPreferencesChanged = { newPrefs ->
                            preferences = newPrefs
                        }
                    )
                }
            }
            
            // Data Management Section
            item {
                SettingsSection(title = "Data Management", icon = Icons.Default.ManageAccounts) {
                    DataManagementContent(
                        onExportData = { /* Handle export */ },
                        onImportData = { /* Handle import */ },
                        onClearAllData = { /* Handle clear all */ }
                    )
                }
            }
            
            // Save Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch {
                            ClockManager.updatePreferences(preferences)
                            keyboardController?.hide()
                            onPreferencesUpdated()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Settings")
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            content()
        }
    }
}

@Composable
fun DisplaySettingsContent(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Time Format
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("24-hour format")
            Switch(
                checked = preferences.is24HourFormat,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(is24HourFormat = it))
                }
            )
        }
        
        // Show Seconds
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show seconds")
            Switch(
                checked = preferences.showSeconds,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(showSeconds = it))
                }
            )
        }
        
        // Show Date
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show date")
            Switch(
                checked = preferences.showDate,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(showDate = it))
                }
            )
        }
        
        // Show Time Difference
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show time difference")
            Switch(
                checked = preferences.showTimeDifference,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(showTimeDifference = it))
                }
            )
        }
        
        // Show Country Flags
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show country flags")
            Switch(
                checked = preferences.showCountryFlags,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(showCountryFlags = it))
                }
            )
        }
        
        // Show Day/Night Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show day/night indicator")
            Switch(
                checked = preferences.showDayNightIndicator,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(showDayNightIndicator = it))
                }
            )
        }
        
        // Clock Card Style
        Text("Clock card style:")
        Column(modifier = Modifier.selectableGroup()) {
            listOf("standard", "compact", "detailed").forEach { style ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = preferences.clockCardStyle == style,
                            onClick = { 
                                onPreferencesChanged(preferences.copy(clockCardStyle = style))
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = preferences.clockCardStyle == style,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = style.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun BehaviorSettingsContent(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Auto Refresh
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto refresh")
            Switch(
                checked = preferences.autoRefresh,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(autoRefresh = it))
                }
            )
        }
        
        // Default Clocks on Startup
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show default clocks on startup")
            Switch(
                checked = preferences.defaultClocksOnStartup,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(defaultClocksOnStartup = it))
                }
            )
        }
        
        // Remember Search History
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Remember search history")
            Switch(
                checked = preferences.rememberSearchHistory,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(rememberSearchHistory = it))
                }
            )
        }
    }
}

@Composable
fun ThemeSettingsContent(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Theme mode:")
        Column(modifier = Modifier.selectableGroup()) {
            listOf("light", "dark", "auto").forEach { theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = preferences.themeMode == theme,
                            onClick = { 
                                onPreferencesChanged(preferences.copy(themeMode = theme))
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = preferences.themeMode == theme,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = theme.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        
        // Dynamic Colors (Android)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dynamic colors (Material You)")
            Switch(
                checked = preferences.dynamicColors,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(dynamicColors = it))
                }
            )
        }
    }
}

@Composable
fun NotificationSettingsContent(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Enable Notifications
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable notifications")
            Switch(
                checked = preferences.enableNotifications,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(enableNotifications = it))
                }
            )
        }
        
        if (preferences.enableNotifications) {
            // Notification Sound
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notification sound")
                Switch(
                    checked = preferences.notificationSound,
                    onCheckedChange = { 
                        onPreferencesChanged(preferences.copy(notificationSound = it))
                    }
                )
            }
            
            // Notification Vibration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Notification vibration")
                Switch(
                    checked = preferences.notificationVibration,
                    onCheckedChange = { 
                        onPreferencesChanged(preferences.copy(notificationVibration = it))
                    }
                )
            }
        }
    }
}

@Composable
fun StorageSettingsContent(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Auto Backup
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Auto backup")
            Switch(
                checked = preferences.autoBackup,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(autoBackup = it))
                }
            )
        }
        
        if (preferences.autoBackup) {
            Text("Backup frequency:")
            Column(modifier = Modifier.selectableGroup()) {
                listOf("daily", "weekly", "monthly").forEach { frequency ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = preferences.backupFrequency == frequency,
                                onClick = { 
                                    onPreferencesChanged(preferences.copy(backupFrequency = frequency))
                                },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = preferences.backupFrequency == frequency,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = frequency.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchSettingsContent(
    preferences: UserPreferences,
    onPreferencesChanged: (UserPreferences) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Search filters:")
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show favorites only")
            Switch(
                checked = preferences.searchFilterFavorites,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(searchFilterFavorites = it))
                }
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show custom clocks only")
            Switch(
                checked = preferences.searchFilterCustom,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(searchFilterCustom = it))
                }
            )
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show popular locations")
            Switch(
                checked = preferences.searchFilterPopular,
                onCheckedChange = { 
                    onPreferencesChanged(preferences.copy(searchFilterPopular = it))
                }
            )
        }
    }
}

@Composable
fun DataManagementContent(
    onExportData: () -> Unit,
    onImportData: () -> Unit,
    onClearAllData: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onExportData,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.FileDownload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Export Data")
        }
        
        Button(
            onClick = onImportData,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.FileUpload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Import Data")
        }
        
        OutlinedButton(
            onClick = onClearAllData,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clear All Data")
        }
    }
}
