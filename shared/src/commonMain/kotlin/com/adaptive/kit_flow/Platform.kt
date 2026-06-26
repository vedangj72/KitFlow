package com.adaptive.kit_flow

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform