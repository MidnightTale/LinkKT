package net.hynse.linkKT.api

import net.hynse.linkKT.event.PlayerRespawnEventFolia
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import me.nahu.scheduler.wrapper.task.WrappedTask
import net.hynse.linkKT.LinkKT.Companion.instance
import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler

class RespawnAPI() : Listener {
    private val respawnTasks = mutableMapOf<String, WrappedTask>()

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val playerName = player.name

        // Cancel any existing task for this player
        respawnTasks[playerName]?.cancel()

        // Start a new task to check for respawn
        val task = wrappedScheduler.runTaskTimerAtEntity(player, {
            if (player.isOnline && !player.isDead) {
                // Player has respawned
                wrappedScheduler.runTaskAsynchronously {
                    instance.server.pluginManager.callEvent(PlayerRespawnEventFolia(player))
                }
                respawnTasks[playerName]?.cancel()
                respawnTasks.remove(playerName)
            }
        }, 1L, 1L)

        task?.let { respawnTasks[playerName] = it }
    }
}