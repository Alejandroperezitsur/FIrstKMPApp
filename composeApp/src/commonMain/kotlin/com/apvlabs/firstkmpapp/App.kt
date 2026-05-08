package com.apvlabs.firstkmpapp

import androidx.compose.animation.core.*
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTimeApi
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
// Extension functions for string operations
fun String.isBlank(): Boolean = this.isEmpty() || this.all { it.isWhitespace() }
fun String.substringBeforeLast(delimiter: String): String = 
    this.lastIndexOf(delimiter).let { if (it == -1) this else this.substring(0, it) }

@Composable
@Preview
fun App() {
    WorldClockTheme {
        WorldClockApp()
    }
}

@Composable
fun WorldClockApp() {
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    val preferences = ClockManager.getPreferences()
    var currentTheme by remember { mutableStateOf(ThemeManager.getThemeMode()) }
    var showSettings by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Initialize storage on app start
    LaunchedEffect(Unit) {
        try {
            ClockManager.initializeStorage()
            isLoading = false
        } catch (e: Exception) {
            println("Error initializing storage: ${e.message}")
            isLoading = false
        }
    }
    
    // Update theme when preferences change
    LaunchedEffect(preferences.themeMode) {
        val themeMode = when (preferences.themeMode) {
            "light" -> ThemeMode.LIGHT
            "dark" -> ThemeMode.DARK
            else -> ThemeMode.AUTO
        }
        currentTheme = themeMode
        ThemeManager.setThemeMode(currentTheme)
    }
    
    // Auto-refresh time
    LaunchedEffect(preferences.autoRefresh, preferences.refreshInterval) {
        if (preferences.autoRefresh) {
            while (true) {
                delay(preferences.refreshInterval * 1000L)
                currentTime = Clock.System.now()
            }
        }
    }
    
    var showAddClockDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    
    // Show loading screen while initializing
    if (isLoading) {
        LoadingScreen()
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Header
                TopAppBar(
                title = {
                    Text(
                        "World Clock",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { showAddClockDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Clock")
                    }
                    IconButton(onClick = { ClockManager.toggleTimeFormat() }) {
                        Icon(
                            if (preferences.is24HourFormat) Icons.Default.Schedule else Icons.Default.AccessTime,
                            contentDescription = "Toggle Time Format"
                        )
                    }
                    IconButton(onClick = { 
                        currentTheme = when (currentTheme) {
                            ThemeMode.LIGHT -> ThemeMode.DARK
                            ThemeMode.DARK -> ThemeMode.AUTO
                            ThemeMode.AUTO -> ThemeMode.LIGHT
                        }
                    }) {
                        val icon = when (currentTheme) {
                            ThemeMode.LIGHT -> Icons.Default.LightMode
                            ThemeMode.DARK -> Icons.Default.DarkMode
                            ThemeMode.AUTO -> if (isDarkTheme()) Icons.Default.DarkMode else Icons.Default.LightMode
                        }
                        Icon(icon, contentDescription = "Toggle Theme")
                    }
                    IconButton(onClick = { showSettings = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            
            // Search Bar
            if (showSearch) {
                EnhancedSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { query ->
                        ClockManager.addToSearchHistory(query)
                    },
                    onActiveChange = { showSearch = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    placeholder = { Text("Search clocks...") },
                    active = false
                )
            }
            
            // Clocks List
            val clocks = if (searchQuery.isBlank()) {
                ClockManager.getClocks()
            } else {
                ClockManager.searchClocks(searchQuery)
            }
            
            if (clocks.isEmpty()) {
                EmptyState(onAddClock = { showAddClockDialog = true })
            } else {
                ClocksList(
                    clocks = clocks,
                    currentTime = currentTime,
                    onRemoveClock = { clockId -> ClockManager.removeClock(clockId) },
                    onToggleFavorite = { clockId -> ClockManager.toggleFavorite(clockId) }
                )
            }
        }
        
        // Add Clock Dialog
        if (showAddClockDialog) {
            AddClockDialog(
                onDismiss = { showAddClockDialog = false },
                onAddClock = { location ->
                    ClockManager.addClock(location)
                    showAddClockDialog = false
                }
            )
        }
        
        // Settings Screen
        if (showSettings) {
            SettingsScreen(
                onBack = { showSettings = false },
                onPreferencesUpdated = {
                    // Preferences are already updated in ClockManager
                    showSettings = false
                }
            )
        }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading World Clock...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ClocksList(
    clocks: List<WorldClock>,
    currentTime: kotlinx.datetime.Instant,
    onRemoveClock: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val preferences = ClockManager.getPreferences()
    
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = clocks,
            key = { it.id }
        ) { clock ->
            AnimatedEntry(
                visible = true,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) {
                ClockCard(
                    clock = clock,
                    currentTime = currentTime,
                    onRemoveClock = onRemoveClock,
                    onToggleFavorite = onToggleFavorite,
                    preferences = preferences
                )
            }
        }
    }
}

@Composable
fun AnimatedEntry(
    visible: Boolean,
    animationSpec: AnimationSpec<Float> = tween(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = animationSpec
        ) + fadeIn(
            animationSpec = animationSpec
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = animationSpec
        ) + fadeOut(
            animationSpec = animationSpec
        ),
        content = content
    )
}

@Composable
fun ClockCard(
    clock: WorldClock,
    currentTime: kotlinx.datetime.Instant,
    onRemoveClock: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    preferences: UserPreferences
) {
    val isDayTime = TimeService.isDayTime(clock.location)
    val backgroundColor = if (isDayTime) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with location and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    // Country flag or location icon (if enabled)
                    if (preferences.showCountryFlags) {
                        Text(
                            text = CountryFlagService.getLocationIcon(clock.location),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = clock.location.city,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = clock.location.country,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Row {
                    // Day/Night indicator (if enabled)
                    if (preferences.showDayNightIndicator) {
                        Icon(
                            imageVector = if (isDayTime) Icons.Default.WbSunny else Icons.Default.NightsStay,
                            contentDescription = if (isDayTime) "Day" else "Night",
                            tint = if (isDayTime) Color(0xFFFF9800) else Color(0xFF2196F3),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Favorite button
                    IconButton(
                        onClick = { onToggleFavorite(clock.id) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (clock.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (clock.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (clock.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // Remove button (don't show for local time)
                    if (!clock.isLocalTime) {
                        IconButton(
                            onClick = { onRemoveClock(clock.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove clock",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Time display
            val timeString = TimeService.getCurrentTimeForLocation(clock.location, preferences.is24HourFormat)
            val displayTime = if (preferences.showSeconds) timeString else timeString.substringBeforeLast(":")
            
            Text(
                text = displayTime,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            // Date display
            if (preferences.showDate) {
                Text(
                    text = TimeService.getCurrentDateForLocation(clock.location),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            // Timezone and difference
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = TimeService.getTimeZoneString(clock.location),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (preferences.showTimeDifference && !clock.isLocalTime) {
                    Text(
                        text = TimeService.getTimeDifferenceFromLocal(clock.location),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(onAddClock: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No clocks added yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add your first world clock to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddClock) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Clock")
        }
    }
}