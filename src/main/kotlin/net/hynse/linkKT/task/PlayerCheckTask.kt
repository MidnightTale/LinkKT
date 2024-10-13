package net.hynse.linkKT.task

import net.hynse.linkKT.LinkKT.Companion.playerManager
import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler
import net.hynse.linkKT.LinkKT.Companion.config
import net.hynse.linkKT.LinkKT.Companion.boostManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import me.nahu.scheduler.wrapper.runnable.WrappedRunnable

class PlayerCheckTask : WrappedRunnable() {
    private val playerTiers = mutableMapOf<Player, Int>()

    override fun run() {
        Bukkit.getOnlinePlayers().forEach { player ->
            schedulePlayerCheck(player)
        }
    }

    private fun schedulePlayerCheck(player: Player) {
        wrappedScheduler.runTaskAtLocation(player.location) {
            val nearbyPlayers = player.getNearbyEntities(5.0, 5.0, 5.0)
                .filterIsInstance<Player>()
                .filter { it != player }

            playerManager.setNearbyPlayers(player, nearbyPlayers)
            playerManager.setPlayerCount(player.uniqueId, nearbyPlayers.size)

            val currentTier = config.tiers.entries.firstOrNull { it.value.playersNearby == nearbyPlayers.size }?.key ?: 0
            val previousTier = playerTiers.getOrDefault(player, 0)

            if (currentTier != previousTier) {
                boostManager.applyBoost(player, nearbyPlayers.size)
                playerTiers[player] = currentTier
            }
        }
    }
}