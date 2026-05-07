package com.apvlabs.firstkmpapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var selectedCountry by remember { mutableStateOf("") }
        var selectedCity by remember { mutableStateOf("") }
        var timeAtLocation by remember { mutableStateOf("No location selected") }
        var currentTime by remember { mutableStateOf("") }
        var showTime by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Local Time Application",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Simple Country Selection
            OutlinedTextField(
                value = selectedCountry,
                onValueChange = { selectedCountry = it },
                label = { Text("Select Country") },
                modifier = Modifier.fillMaxWidth()
            )

            // Simple City Selection
            OutlinedTextField(
                value = selectedCity,
                onValueChange = { selectedCity = it },
                label = { Text("Select City") },
                modifier = Modifier.fillMaxWidth()
            )

            // Show Time Button
            Button(
                onClick = { 
                    showTime = true
                    val location = LocationData.getLocationByCountryAndCity(selectedCountry, selectedCity)
                    if (location != null) {
                        currentTime = TimeService.getCurrentTimeForLocation(location)
                        timeAtLocation = "Current time in ${TimeService.getFormattedLocationInfo(location)}:\n" +
                                "$currentTime (${TimeService.getTimeZoneString(location)})"
                    } else {
                        timeAtLocation = "Location not found"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show Time")
            }

            // Time Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (currentTime.isNotEmpty()) currentTime else "--:--:--",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = timeAtLocation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Instructions
            if (!showTime) {
                Text(
                    text = "Please select a country and city, then click 'Show Time'",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}