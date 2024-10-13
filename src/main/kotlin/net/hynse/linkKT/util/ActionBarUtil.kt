package net.hynse.linkKT.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

object ActionBarUtil {
    fun updateActionBar(player: Player, nearbyPlayers: List<Player>, nearbyPlayerCount: Int) {
        val nearbyPlayerNames = nearbyPlayers.joinToString(", ") { it.name }

        val actionBar: Component = Component.text("Nearby players: $nearbyPlayerCount", NamedTextColor.GOLD)
            .append(Component.text(" | $nearbyPlayerNames", NamedTextColor.AQUA))
            .append(Component.text(" | " + getBoostMessage(nearbyPlayerCount), NamedTextColor.GREEN))
        player.sendActionBar(actionBar)
    }

    private fun getBoostMessage(count: Int): String {
        return when (count) {
            1 -> "Mining Speed+"
            2 -> "Luck+"
            else -> "No Boost"
        }
    }
}