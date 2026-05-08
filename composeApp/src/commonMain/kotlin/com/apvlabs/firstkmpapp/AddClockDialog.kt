package com.apvlabs.firstkmpapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClockDialog(
    onDismiss: () -> Unit,
    onAddClock: (Location) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var showCountryDropdown by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("") }
    var showCityDropdown by remember { mutableStateOf(false) }
    
    val keyboardController = LocalSoftwareKeyboardController.current
    val countries = LocationData.getCountryNames()
    val cities = if (selectedCountry.isNotEmpty()) {
        LocationData.getCitiesByCountry(selectedCountry)
    } else emptyList()
    val searchResults = if (searchQuery.isNotEmpty()) {
        LocationData.searchLocations(searchQuery)
    } else emptyList()
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Añadir Reloj Mundial",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar por ciudad o país") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { keyboardController?.hide() }
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Country selection
                ExposedDropdownMenuBox(
                    expanded = showCountryDropdown,
                    onExpandedChange = { showCountryDropdown = !showCountryDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedCountry,
                        onValueChange = { 
                            selectedCountry = it
                            selectedCity = ""
                        },
                        label = { Text("Seleccionar País") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCountryDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true
                    )
                    
                    ExposedDropdownMenu(
                        expanded = showCountryDropdown,
                        onDismissRequest = { showCountryDropdown = false }
                    ) {
                        Column(
                            modifier = Modifier
                                .heightIn(max = 200.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            countries.forEach { country ->
                                DropdownMenuItem(
                                    text = { Text(country) },
                                    onClick = {
                                        selectedCountry = country
                                        selectedCity = ""
                                        showCountryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // City selection
                if (selectedCountry.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = showCityDropdown,
                        onExpandedChange = { showCityDropdown = !showCityDropdown }
                    ) {
                        OutlinedTextField(
                            value = selectedCity,
                            onValueChange = { selectedCity = it },
                            label = { Text("Seleccionar Ciudad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCityDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            readOnly = true
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showCityDropdown,
                            onDismissRequest = { showCityDropdown = false }
                        ) {
                            Column(
                                modifier = Modifier
                                    .heightIn(max = 200.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                cities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city) },
                                        onClick = {
                                            selectedCity = city
                                            showCityDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search results or popular locations
                if (searchQuery.isNotEmpty() || (selectedCountry.isEmpty() && selectedCity.isEmpty())) {
                    val locationsToShow = if (searchQuery.isNotEmpty()) {
                        searchResults
                    } else {
                        LocationData.getPopularLocations()
                    }
                    
                    Text(
                        text = if (searchQuery.isNotEmpty()) "Resultados de búsqueda" else "Ubicaciones populares",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                        items(locationsToShow) { location ->
                            LocationItem(
                                location = location,
                                onLocationSelected = {
                                    onAddClock(location)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (selectedCountry.isNotEmpty() && selectedCity.isNotEmpty()) {
                                val location = LocationData.getLocationByCountryAndCity(selectedCountry, selectedCity)
                                location?.let {
                                    onAddClock(it)
                                    onDismiss()
                                }
                            }
                        },
                        enabled = selectedCountry.isNotEmpty() && selectedCity.isNotEmpty()
                    ) {
                        Text("Añadir Reloj")
                    }
                }
            }
        }
    }
}

@Composable
fun LocationItem(
    location: Location,
    onLocationSelected: (Location) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onLocationSelected(location) },
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
                // Country flag
                Text(
                    text = CountryFlagService.getCountryFlag(location.country),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
                
                Column {
                    Text(
                        text = location.city,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = location.country,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Text(
                text = TimeService.getTimeZoneString(location),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
