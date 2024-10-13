package net.hynse.linkKT.task

import me.nahu.scheduler.wrapper.runnable.WrappedRunnable
import net.hynse.linkKT.LinkKT.Companion.boostManager
import net.hynse.linkKT.LinkKT.Companion.config
import net.hynse.linkKT.LinkKT.Companion.playerManager
import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerCheckTask() : WrappedRunnable() {
    override fun run() {
        for (p in Bukkit.getOnlinePlayers()) {
            val location = p.location
            wrappedScheduler.runTaskAtLocation(location, Runnable { processPlayer(p) })
        }
    }

    private fun processPlayer(player: Player) {
        val nearbyPlayers = getNearbyPlayers(player)
        val nearbyPlayerCount = nearbyPlayers.size
        val uuid = player.uniqueId

        val currentCount = playerManager.getPlayerCount(uuid)
        if (currentCount != nearbyPlayerCount) {
            playerManager.setPlayerCount(uuid, nearbyPlayerCount)
            wrappedScheduler.runTaskAtEntity(player, Runnable {
                if (nearbyPlayerCount == 0) {
                    // Remove all boosts if there are no nearby players
                    boostManager.removeAllBoosts(player)
                } else {
                    // Apply boost based on the number of nearby players
                    boostManager.applyBoost(player, nearbyPlayerCount)
                }
            })
        }
        playerManager.setNearbyPlayers(player, nearbyPlayers)
    }

    private fun getNearbyPlayers(player: Player): List<Player> {
        val radius = config.nearbyRadius.toDouble()
        return player.getNearbyEntities(radius, radius, radius)
            .filterIsInstance<Player>()
            .filter { it != player && it.location.distanceSquared(player.location) <= radius * radius }
    }
}