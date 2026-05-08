package com.apvlabs.firstkmpapp

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

object TimeService {
    
    fun getCurrentTimeForLocation(location: Location, now: Instant = Clock.System.now(), is24Hour: Boolean = true): String {
        return try {
            val timeZone = TimeZone.of(location.timezoneId)
            val dateTimeInZone = now.toLocalDateTime(timeZone)
            
            if (is24Hour) {
                "${padTwoDigits(dateTimeInZone.hour)}:${padTwoDigits(dateTimeInZone.minute)}:${padTwoDigits(dateTimeInZone.second)}"
            } else {
                val hour = dateTimeInZone.hour
                val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val amPm = if (hour < 12) "AM" else "PM"
                "${padTwoDigits(displayHour)}:${padTwoDigits(dateTimeInZone.minute)}:${padTwoDigits(dateTimeInZone.second)} $amPm"
            }
        } catch (e: Exception) {
            "00:00:00"
        }
    }
    
    fun getCurrentDateForLocation(location: Location, now: Instant = Clock.System.now()): String {
        return try {
            val timeZone = TimeZone.of(location.timezoneId)
            val dateTimeInZone = now.toLocalDateTime(timeZone)
            "${dateTimeInZone.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${dateTimeInZone.dayOfMonth}, ${dateTimeInZone.year}"
        } catch (e: Exception) {
            "Unknown Date"
        }
    }
    
    fun getTimeZoneString(location: Location, now: Instant = Clock.System.now()): String {
        return try {
            val timeZone = TimeZone.of(location.timezoneId)
            val offset = timeZone.offsetAt(now)
            val totalSeconds = offset.totalSeconds
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val sign = if (hours >= 0) "+" else ""
            "$sign${hours}:${padTwoDigits(minutes)}"
        } catch (e: Exception) {
            val offset = location.utcOffsetHours
            val sign = if (offset >= 0) "+" else ""
            val absOffset = if (offset < 0) -offset else offset
            "UTC$sign${padTwoDigits(absOffset)}:00"
        }
    }
    
    fun getTimeZoneAbbreviation(location: Location, now: Instant = Clock.System.now()): String {
        return try {
            val timeZone = TimeZone.of(location.timezoneId)
            timeZone.id.split("/").last().replace("_", " ")
        } catch (e: Exception) {
            location.timezoneId.split("/").last().replace("_", " ")
        }
    }
    
    fun getFormattedLocationInfo(location: Location): String {
        return "${location.city}, ${location.country}"
    }
    
    fun isDayTime(location: Location, now: Instant = Clock.System.now()): Boolean {
        return try {
            val timeZone = TimeZone.of(location.timezoneId)
            val dateTimeInZone = now.toLocalDateTime(timeZone)
            val hour = dateTimeInZone.hour
            hour in 6..18 // Consider daytime from 6 AM to 6 PM
        } catch (e: Exception) {
            true
        }
    }
    
    fun getTimeDifferenceFromLocal(location: Location, now: Instant = Clock.System.now()): String {
        return try {
            val localTimeZone = TimeZone.currentSystemDefault()
            val targetTimeZone = TimeZone.of(location.timezoneId)
            
            val localOffset = localTimeZone.offsetAt(now)
            val targetOffset = targetTimeZone.offsetAt(now)
            
            val diffMinutes = (targetOffset.totalSeconds - localOffset.totalSeconds) / 60
            val diffHours = diffMinutes / 60
            val remainingMinutes = diffMinutes % 60
            
            if (diffHours == 0 && remainingMinutes == 0) {
                "Same time"
            } else if (diffHours > 0) {
                val hoursText = if (diffHours == 1) "hour" else "hours"
                val minutesText = if (remainingMinutes == 1) "minute" else "minutes"
                if (remainingMinutes == 0) {
                    "+$diffHours $hoursText ahead"
                } else {
                    "+$diffHours $hoursText $remainingMinutes $minutesText ahead"
                }
            } else {
                val absHours = -diffHours
                val absMinutes = -remainingMinutes
                val hoursText = if (absHours == 1) "hour" else "hours"
                val minutesText = if (absMinutes == 1) "minute" else "minutes"
                if (absMinutes == 0) {
                    "$absHours $hoursText behind"
                } else {
                    "$absHours $hoursText $absMinutes $minutesText behind"
                }
            }
        } catch (e: Exception) {
            "Unknown difference"
        }
    }
    
    private fun padTwoDigits(number: Int): String {
        return if (number < 10) "0$number" else "$number"
    }
}
