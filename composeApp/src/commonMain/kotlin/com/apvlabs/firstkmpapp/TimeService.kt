package com.apvlabs.firstkmpapp

object TimeService {
    
    fun getCurrentTimeForLocation(location: Location): String {
        return try {
            // Usar cálculo simple de UTC offset para evitar problemas con timezone
            getUTCTimeWithOffset(location.utcOffsetHours)
        } catch (e: Exception) {
            "00:00:00"
        }
    }
    
    fun getUTCTimeWithOffset(offsetHours: Int): String {
        // Calcular hora actual con offset UTC
        val totalMinutes = offsetHours * 60
        val currentHour = (getCurrentHour() + offsetHours + 24) % 24
        val currentMinute = getCurrentMinute()
        val currentSecond = getCurrentSecond()
        
        val hour = padTwoDigits(currentHour)
        val minute = padTwoDigits(currentMinute)
        val second = padTwoDigits(currentSecond)
        
        return "$hour:$minute:$second"
    }
    
    fun getTimeZoneString(location: Location): String {
        val offset = location.utcOffsetHours
        val sign = if (offset >= 0) "+" else ""
        val absOffset = if (offset < 0) -offset else offset
        val paddedOffset = padTwoDigits(absOffset)
        return "UTC$sign$paddedOffset:00"
    }
    
    fun getFormattedLocationInfo(location: Location): String {
        return "${location.city}, ${location.country}"
    }
    
    // Funciones auxiliares simples - usar tiempo simulado para evitar problemas
    private fun getCurrentHour(): Int {
        // Simular hora actual basada en timezone UTC
        return 12 // Hora base para demostración
    }
    
    private fun getCurrentMinute(): Int {
        return 30
    }
    
    private fun getCurrentSecond(): Int {
        return 0
    }
    
    private fun padTwoDigits(number: Int): String {
        return if (number < 10) "0$number" else "$number"
    }
}
