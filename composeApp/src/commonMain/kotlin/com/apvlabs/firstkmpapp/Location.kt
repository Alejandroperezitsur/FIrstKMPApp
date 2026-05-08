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
        // North America
        Location("United States", "New York", "America/New_York", -5),
        Location("United States", "Los Angeles", "America/Los_Angeles", -8),
        Location("United States", "Chicago", "America/Chicago", -6),
        Location("United States", "Houston", "America/Chicago", -6),
        Location("United States", "Phoenix", "America/Phoenix", -7),
        Location("United States", "Philadelphia", "America/New_York", -5),
        Location("United States", "San Francisco", "America/Los_Angeles", -8),
        Location("United States", "Boston", "America/New_York", -5),
        Location("United States", "Washington D.C.", "America/New_York", -5),
        Location("United States", "Miami", "America/New_York", -5),
        Location("United States", "Seattle", "America/Los_Angeles", -8),
        Location("United States", "Denver", "America/Denver", -7),
        Location("United States", "Atlanta", "America/New_York", -5),
        Location("Canada", "Toronto", "America/Toronto", -5),
        Location("Canada", "Vancouver", "America/Vancouver", -8),
        Location("Canada", "Montreal", "America/Montreal", -5),
        Location("Canada", "Calgary", "America/Edmonton", -7),
        Location("Canada", "Ottawa", "America/Toronto", -5),
        Location("Mexico", "Mexico City", "America/Mexico_City", -6),
        Location("Mexico", "Guadalajara", "America/Mexico_City", -6),
        Location("Mexico", "Monterrey", "America/Monterrey", -6),
        
        // South America
        Location("Brazil", "São Paulo", "America/Sao_Paulo", -3),
        Location("Brazil", "Rio de Janeiro", "America/Sao_Paulo", -3),
        Location("Brazil", "Brasília", "America/Sao_Paulo", -3),
        Location("Argentina", "Buenos Aires", "America/Argentina/Buenos_Aires", -3),
        Location("Chile", "Santiago", "America/Santiago", -4),
        Location("Colombia", "Bogotá", "America/Bogota", -5),
        Location("Peru", "Lima", "America/Lima", -5),
        Location("Venezuela", "Caracas", "America/Caracas", -4),
        
        // Europe
        Location("United Kingdom", "London", "Europe/London", 0),
        Location("United Kingdom", "Manchester", "Europe/London", 0),
        Location("Germany", "Berlin", "Europe/Berlin", 1),
        Location("Germany", "Munich", "Europe/Berlin", 1),
        Location("France", "Paris", "Europe/Paris", 1),
        Location("France", "Lyon", "Europe/Paris", 1),
        Location("Italy", "Rome", "Europe/Rome", 1),
        Location("Italy", "Milan", "Europe/Rome", 1),
        Location("Spain", "Madrid", "Europe/Madrid", 1),
        Location("Spain", "Barcelona", "Europe/Madrid", 1),
        Location("Netherlands", "Amsterdam", "Europe/Amsterdam", 1),
        Location("Belgium", "Brussels", "Europe/Brussels", 1),
        Location("Switzerland", "Zurich", "Europe/Zurich", 1),
        Location("Austria", "Vienna", "Europe/Vienna", 1),
        Location("Sweden", "Stockholm", "Europe/Stockholm", 1),
        Location("Norway", "Oslo", "Europe/Oslo", 1),
        Location("Denmark", "Copenhagen", "Europe/Copenhagen", 1),
        Location("Finland", "Helsinki", "Europe/Helsinki", 2),
        Location("Poland", "Warsaw", "Europe/Warsaw", 1),
        Location("Czech Republic", "Prague", "Europe/Prague", 1),
        Location("Hungary", "Budapest", "Europe/Budapest", 1),
        Location("Portugal", "Lisbon", "Europe/Lisbon", 0),
        Location("Greece", "Athens", "Europe/Athens", 2),
        Location("Russia", "Moscow", "Europe/Moscow", 3),
        Location("Russia", "St. Petersburg", "Europe/Moscow", 3),
        Location("Turkey", "Istanbul", "Europe/Istanbul", 3),
        Location("Turkey", "Ankara", "Europe/Istanbul", 3),
        
        // Asia
        Location("China", "Beijing", "Asia/Shanghai", 8),
        Location("China", "Shanghai", "Asia/Shanghai", 8),
        Location("China", "Guangzhou", "Asia/Shanghai", 8),
        Location("China", "Shenzhen", "Asia/Shanghai", 8),
        Location("Japan", "Tokyo", "Asia/Tokyo", 9),
        Location("Japan", "Osaka", "Asia/Tokyo", 9),
        Location("South Korea", "Seoul", "Asia/Seoul", 9),
        Location("South Korea", "Busan", "Asia/Seoul", 9),
        Location("India", "New Delhi", "Asia/Kolkata", 5),
        Location("India", "Mumbai", "Asia/Kolkata", 5),
        Location("India", "Bangalore", "Asia/Kolkata", 5),
        Location("India", "Kolkata", "Asia/Kolkata", 5),
        Location("Indonesia", "Jakarta", "Asia/Jakarta", 7),
        Location("Indonesia", "Surabaya", "Asia/Jakarta", 7),
        Location("Thailand", "Bangkok", "Asia/Bangkok", 7),
        Location("Singapore", "Singapore", "Asia/Singapore", 8),
        Location("Malaysia", "Kuala Lumpur", "Asia/Kuala_Lumpur", 8),
        Location("Philippines", "Manila", "Asia/Manila", 8),
        Location("Vietnam", "Ho Chi Minh City", "Asia/Ho_Chi_Minh", 7),
        Location("Pakistan", "Karachi", "Asia/Karachi", 5),
        Location("Bangladesh", "Dhaka", "Asia/Dhaka", 6),
        Location("Sri Lanka", "Colombo", "Asia/Colombo", 5),
        Location("Myanmar", "Yangon", "Asia/Yangon", 6),
        Location("Cambodia", "Phnom Penh", "Asia/Phnom_Penh", 7),
        Location("Laos", "Vientiane", "Asia/Vientiane", 7),
        
        // Middle East
        Location("United Arab Emirates", "Dubai", "Asia/Dubai", 4),
        Location("United Arab Emirates", "Abu Dhabi", "Asia/Dubai", 4),
        Location("Saudi Arabia", "Riyadh", "Asia/Riyadh", 3),
        Location("Israel", "Tel Aviv", "Asia/Tel_Aviv", 2),
        Location("Israel", "Jerusalem", "Asia/Jerusalem", 2),
        Location("Jordan", "Amman", "Asia/Amman", 2),
        Location("Lebanon", "Beirut", "Asia/Beirut", 2),
        Location("Qatar", "Doha", "Asia/Qatar", 3),
        Location("Kuwait", "Kuwait City", "Asia/Kuwait", 3),
        Location("Bahrain", "Manama", "Asia/Bahrain", 3),
        Location("Oman", "Muscat", "Asia/Muscat", 4),
        Location("Iran", "Tehran", "Asia/Tehran", 3),
        Location("Iraq", "Baghdad", "Asia/Baghdad", 3),
        
        // Africa
        Location("South Africa", "Johannesburg", "Africa/Johannesburg", 2),
        Location("South Africa", "Cape Town", "Africa/Johannesburg", 2),
        Location("South Africa", "Pretoria", "Africa/Johannesburg", 2),
        Location("Egypt", "Cairo", "Africa/Cairo", 2),
        Location("Egypt", "Alexandria", "Africa/Cairo", 2),
        Location("Nigeria", "Lagos", "Africa/Lagos", 1),
        Location("Kenya", "Nairobi", "Africa/Nairobi", 3),
        Location("Morocco", "Casablanca", "Africa/Casablanca", 1),
        Location("Ethiopia", "Addis Ababa", "Africa/Addis_Ababa", 3),
        Location("Ghana", "Accra", "Africa/Accra", 0),
        Location("Tanzania", "Dar es Salaam", "Africa/Dar_es_Salaam", 3),
        Location("Uganda", "Kampala", "Africa/Kampala", 3),
        
        // Oceania
        Location("Australia", "Sydney", "Australia/Sydney", 11),
        Location("Australia", "Melbourne", "Australia/Melbourne", 11),
        Location("Australia", "Brisbane", "Australia/Brisbane", 10),
        Location("Australia", "Perth", "Australia/Perth", 8),
        Location("Australia", "Adelaide", "Australia/Adelaide", 10),
        Location("Australia", "Canberra", "Australia/Sydney", 11),
        Location("New Zealand", "Auckland", "Pacific/Auckland", 13),
        Location("New Zealand", "Wellington", "Pacific/Auckland", 13),
        Location("Fiji", "Suva", "Pacific/Fiji", 12),
        
        // Central America & Caribbean
        Location("Panama", "Panama City", "America/Panama", -5),
        Location("Costa Rica", "San José", "America/Costa_Rica", -6),
        Location("Guatemala", "Guatemala City", "America/Guatemala", -6),
        Location("Cuba", "Havana", "America/Havana", -5),
        Location("Jamaica", "Kingston", "America/Jamaica", -5),
        Location("Puerto Rico", "San Juan", "America/Puerto_Rico", -4),
        Location("Dominican Republic", "Santo Domingo", "America/Santo_Domingo", -4),
        
        // Popular tourist/business destinations
        Location("Iceland", "Reykjavik", "Atlantic/Reykjavik", 0),
        Location("Ireland", "Dublin", "Europe/Dublin", 0),
        Location("Luxembourg", "Luxembourg", "Europe/Luxembourg", 1),
        Location("Monaco", "Monaco", "Europe/Monaco", 1),
        Location("Malta", "Valletta", "Europe/Malta", 1),
        Location("Cyprus", "Nicosia", "Asia/Nicosia", 2),
        Location("Georgia", "Tbilisi", "Asia/Tbilisi", 4),
        Location("Armenia", "Yerevan", "Asia/Yerevan", 4),
        Location("Azerbaijan", "Baku", "Asia/Baku", 4),
        Location("Kazakhstan", "Almaty", "Asia/Almaty", 6),
        Location("Uzbekistan", "Tashkent", "Asia/Tashkent", 5),
        Location("Afghanistan", "Kabul", "Asia/Kabul", 4),
        Location("Nepal", "Kathmandu", "Asia/Kathmandu", 5),
        Location("Bhutan", "Thimphu", "Asia/Thimphu", 6),
        Location("Mongolia", "Ulaanbaatar", "Asia/Ulaanbaatar", 8),
        Location("North Korea", "Pyongyang", "Asia/Pyongyang", 9)
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
    
    fun searchLocations(query: String): List<Location> {
        if (query.isBlank()) return emptyList()
        
        val lowercaseQuery = query.lowercase()
        return locations.filter { location ->
            location.country.lowercase().contains(lowercaseQuery) ||
            location.city.lowercase().contains(lowercaseQuery)
        }.sortedWith(compareBy<Location> { it.country }.thenBy { it.city })
    }
    
    fun getPopularLocations(): List<Location> {
        return listOf(
            getLocationByCountryAndCity("United States", "New York"),
            getLocationByCountryAndCity("United Kingdom", "London"),
            getLocationByCountryAndCity("Japan", "Tokyo"),
            getLocationByCountryAndCity("France", "Paris"),
            getLocationByCountryAndCity("Australia", "Sydney"),
            getLocationByCountryAndCity("China", "Beijing"),
            getLocationByCountryAndCity("Germany", "Berlin"),
            getLocationByCountryAndCity("India", "New Delhi")
        ).filterNotNull()
    }
}
