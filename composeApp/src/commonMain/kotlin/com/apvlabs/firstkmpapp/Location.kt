package com.apvlabs.firstkmpapp

import kotlinx.datetime.TimeZone

data class Location(
    val country: String,
    val city: String,
    val timezoneId: String,
    val utcOffsetHours: Int
)

object LocationData {
    val locations = listOf(
        Location("United States", "Washington D.C.", "America/New_York", -5),
        Location("United Kingdom", "London", "Europe/London", 0),
        Location("France", "Paris", "Europe/Paris", 1),
        Location("Germany", "Berlin", "Europe/Berlin", 1),
        Location("Japan", "Tokyo", "Asia/Tokyo", 9),
        Location("China", "Beijing", "Asia/Shanghai", 8),
        Location("Australia", "Canberra", "Australia/Sydney", 11),
        Location("Canada", "Ottawa", "America/Toronto", -5),
        Location("Mexico", "Mexico City", "America/Mexico_City", -6),
        Location("Brazil", "Brasília", "America/Sao_Paulo", -3),
        Location("India", "New Delhi", "Asia/Kolkata", 5),
        Location("Russia", "Moscow", "Europe/Moscow", 3),
        Location("South Africa", "Pretoria", "Africa/Johannesburg", 2),
        Location("Egypt", "Cairo", "Africa/Cairo", 2),
        Location("Argentina", "Buenos Aires", "America/Argentina/Buenos_Aires", -3),
        Location("Spain", "Madrid", "Europe/Madrid", 1),
        Location("Italy", "Rome", "Europe/Rome", 1),
        Location("South Korea", "Seoul", "Asia/Seoul", 9),
        Location("Indonesia", "Jakarta", "Asia/Jakarta", 7),
        Location("Turkey", "Ankara", "Europe/Istanbul", 3)
    )
    
    fun getLocationByCountryAndCity(country: String, city: String): Location? {
        return locations.find { 
            it.country.equals(country, ignoreCase = true) && 
            it.city.equals(city, ignoreCase = true) 
        }
    }
    
    fun getCountryNames(): List<String> {
        return locations.map { it.country }.distinct().sorted()
    }
    
    fun getCitiesByCountry(country: String): List<String> {
        return locations.filter { it.country.equals(country, ignoreCase = true) }
            .map { it.city }
            .sorted()
    }
}
