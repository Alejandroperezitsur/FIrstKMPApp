package com.apvlabs.firstkmpapp

import kotlin.collections.mapOf
import kotlin.to

object CountryFlagService {
    
    private val countryToFlag = mapOf(
        // North America
        "United States" to "🇺🇸",
        "Canada" to "🇨🇦",
        "Mexico" to "🇲🇽",
        
        // South America
        "Brazil" to "🇧🇷",
        "Argentina" to "🇦🇷",
        "Chile" to "🇨🇱",
        "Colombia" to "🇨🇴",
        "Peru" to "🇵🇪",
        "Venezuela" to "🇻🇪",
        
        // Europe
        "United Kingdom" to "🇬🇧",
        "Germany" to "🇩🇪",
        "France" to "🇫🇷",
        "Italy" to "🇮🇹",
        "Spain" to "🇪🇸",
        "Netherlands" to "🇳🇱",
        "Belgium" to "🇧🇪",
        "Switzerland" to "🇨🇭",
        "Austria" to "🇦🇹",
        "Sweden" to "🇸🇪",
        "Norway" to "🇳🇴",
        "Denmark" to "🇩🇰",
        "Finland" to "🇫🇮",
        "Poland" to "🇵🇱",
        "Czech Republic" to "🇨🇿",
        "Hungary" to "🇭🇺",
        "Portugal" to "🇵🇹",
        "Greece" to "🇬🇷",
        "Russia" to "🇷🇺",
        "Turkey" to "🇹🇷",
        
        // Asia
        "China" to "🇨🇳",
        "Japan" to "🇯🇵",
        "South Korea" to "🇰🇷",
        "India" to "🇮🇳",
        "Indonesia" to "🇮🇩",
        "Thailand" to "🇹🇭",
        "Singapore" to "🇸🇬",
        "Malaysia" to "🇲🇾",
        "Philippines" to "🇵🇭",
        "Vietnam" to "🇻🇳",
        "Pakistan" to "🇵🇰",
        "Bangladesh" to "🇧🇩",
        "Sri Lanka" to "🇱🇰",
        "Myanmar" to "🇲🇲",
        "Cambodia" to "🇰🇭",
        "Laos" to "🇱🇦",
        
        // Middle East
        "United Arab Emirates" to "🇦🇪",
        "Saudi Arabia" to "🇸🇦",
        "Israel" to "🇮🇱",
        "Jordan" to "🇯🇴",
        "Lebanon" to "🇱🇧",
        "Qatar" to "🇶🇦",
        "Kuwait" to "🇰🇼",
        "Bahrain" to "🇧🇭",
        "Oman" to "🇴🇲",
        "Iran" to "🇮🇷",
        "Iraq" to "🇮🇶",
        
        // Africa
        "South Africa" to "🇿🇦",
        "Egypt" to "🇪🇬",
        "Nigeria" to "🇳🇬",
        "Kenya" to "🇰🇪",
        "Morocco" to "🇲🇦",
        "Ethiopia" to "🇪🇹",
        "Ghana" to "🇬🇭",
        "Tanzania" to "🇹🇿",
        "Uganda" to "🇺🇬",
        
        // Oceania
        "Australia" to "🇦🇺",
        "New Zealand" to "🇳🇿",
        "Fiji" to "🇫🇯",
        
        // Central America & Caribbean
        "Panama" to "🇵🇦",
        "Costa Rica" to "🇨🇷",
        "Guatemala" to "🇬🇹",
        "Cuba" to "🇨🇺",
        "Jamaica" to "🇯🇲",
        "Puerto Rico" to "🇵🇷",
        "Dominican Republic" to "🇩🇴",
        
        // Popular tourist/business destinations
        "Iceland" to "🇮🇸",
        "Ireland" to "🇮🇪",
        "Luxembourg" to "🇱🇺",
        "Monaco" to "🇲🇨",
        "Malta" to "🇲🇹",
        "Cyprus" to "🇨🇾",
        "Georgia" to "🇬🇪",
        "Armenia" to "🇦🇲",
        "Azerbaijan" to "🇦🇿",
        "Kazakhstan" to "🇰🇿",
        "Uzbekistan" to "🇺🇿",
        "Afghanistan" to "🇦🇫",
        "Nepal" to "🇳🇵",
        "Bhutan" to "🇧🇹",
        "Mongolia" to "🇲🇳",
        "North Korea" to "🇰🇵"
    )
    
    fun getCountryFlag(country: String): String {
        return countryToFlag[country] ?: "🌍"
    }
    
    fun getLocationIcon(location: Location): String {
        return if (location.isLocalTime()) {
            "📍"
        } else {
            getCountryFlag(location.country)
        }
    }
    
    private fun Location.isLocalTime(): Boolean {
        return this.country == "Local" && this.city == "Current Time"
    }
}
