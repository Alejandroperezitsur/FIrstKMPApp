package com.apvlabs.firstkmpapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform