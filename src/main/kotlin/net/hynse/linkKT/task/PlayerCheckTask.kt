package net.hynse.linkKT.task

import me.nahu.scheduler.wrapper.runnable.WrappedRunnable
import net.hynse.linkKT.LinkKT.Companion.boostManager
import net.hynse.linkKT.LinkKT.Companion.config
import net.hynse.linkKT.LinkKT.Companion.playerManager
import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler
import net.hynse.linkKT.util.ActionBarUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

class PlayerCheckTask : WrappedRunnable() {

    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            wrappedScheduler.runTaskAtEntity(player, Runnable {
                val nearbyPlayers = getNearbyPlayers(player)
                val nearbyPlayerCount = nearbyPlayers.size
                val previousCount = playerManager.getPlayerCount(player.uniqueId)

                if (nearbyPlayerCount != previousCount) {
                    if (nearbyPlayerCount == 0) {
                        boostManager.removeAllBoosts(player)
                    } else {
                        boostManager.applyBoost(player, nearbyPlayerCount)
                    }
                    playerManager.setPlayerCount(player.uniqueId, nearbyPlayerCount)
                    playerManager.setNearbyPlayers(player.uniqueId, nearbyPlayers.map { it.uniqueId })
                    val actionbar = ActionBarUtil.createActionBarText(nearbyPlayerCount)
                    player.sendActionBar(actionbar)
                }
            })
        }
    }


    private fun getNearbyPlayers(player: Player): List<Player> {
        val radius = config.nearbyRadius.toDouble()
        return player.getNearbyEntities(radius, radius, radius)
            .filterIsInstance<Player>()
            .filter { it != player && it.location.distanceSquared(player.location) <= radius * radius }
            .take(5)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        boostManager.removeAllBoosts(player)
    }
}