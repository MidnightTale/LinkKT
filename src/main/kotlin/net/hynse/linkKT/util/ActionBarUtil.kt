package net.hynse.linkKT.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

object ActionBarUtil {
    private val GOLD = TextColor.fromHexString("#FFD700")
    private val AQUA = TextColor.fromHexString("#00FFFF")
    private val WHITE = TextColor.fromHexString("#FFFFFF")
    private val GRAY = TextColor.fromHexString("#AAAAAA")
    private val GREEN = TextColor.fromHexString("#00FF00")
    private val YELLOW = TextColor.fromHexString("#FFFF00")

    fun updateActionBar(player: Player, nearbyPlayers: List<Player>, nearbyPlayerCount: Int) {
        val actionBar = Component.empty()
            .append(createCountComponent(nearbyPlayerCount))
            .append(Component.text(" | ", GRAY))
            .append(createNearbyPlayersComponent(nearbyPlayers))
            .append(Component.text(" | ", GRAY))
            .append(createBoostComponent(nearbyPlayerCount))

        player.sendActionBar(actionBar)
    }

    private fun createCountComponent(count: Int): Component {
        return Component.text()
            .append(Component.text("Nearby: ", GOLD))
            .append(Component.text(count, WHITE, TextDecoration.BOLD))
            .build()
    }

    private fun createNearbyPlayersComponent(nearbyPlayers: List<Player>): Component {
        val maxDisplayPlayers = 3
        val displayNames = nearbyPlayers.take(maxDisplayPlayers).map { it.name }
        val additionalCount = nearbyPlayers.size - maxDisplayPlayers

        val component = Component.text()
            .append(Component.text("Players: ", AQUA))
            .append(Component.text(displayNames.joinToString(", "), WHITE))

        if (additionalCount > 0) {
            component.append(Component.text(" +$additionalCount more", GRAY, TextDecoration.ITALIC))
        }

        return component.build()
    }

    private fun createBoostComponent(count: Int): Component {
        return Component.text()
            .append(Component.text("Boosts: ", YELLOW))
            .append(getBoostMessage(count))
            .build()
    }

    private fun getBoostMessage(count: Int): Component {
        return when (count) {
            1 -> Component.text()
                .append(Component.text("Speed ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+10% ", WHITE))
                .append(Component.text("Luck ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+0.5 ", WHITE))
                .append(Component.text("Damage ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+5% ", WHITE))
                .append(Component.text("Armor ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+1", WHITE))
                .build()
            2 -> Component.text()
                .append(Component.text("Speed ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+20% ", WHITE))
                .append(Component.text("Luck ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+1.0 ", WHITE))
                .append(Component.text("Damage ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+10% ", WHITE))
                .append(Component.text("Armor ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+2", WHITE))
                .build()
            3 -> Component.text()
                .append(Component.text("Speed ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+30% ", WHITE))
                .append(Component.text("Luck ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+1.5 ", WHITE))
                .append(Component.text("Damage ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+15% ", WHITE))
                .append(Component.text("Armor ", GREEN, TextDecoration.BOLD))
                .append(Component.text("+3", WHITE))
                .build()
            else -> Component.text("No Boost", GRAY)
        }
    }
}