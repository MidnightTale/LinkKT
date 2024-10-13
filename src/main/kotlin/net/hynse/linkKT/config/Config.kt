package net.hynse.linkKT.config

class Config() {
    val nearbyRadius: Double = 16.00
    val playerCheckInterval: Long = 5 // Update every 5 ticks (1/4 second)
    val actionBarUpdateInterval: Long = 20 // Update every 20 ticks (1 second)
    var initialTaskTimer: Long = 1 // Initial task timer in seconds

    // Add more configuration options here
}