package net.hynse.linkKT.manager

import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler
import net.hynse.linkKT.LinkKT.Companion.config
import net.hynse.linkKT.task.ActionBarUpdateTask
import net.hynse.linkKT.task.PlayerCheckTask
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlayerManager {
    private val playerCounts = ConcurrentHashMap<UUID, Int>()
    private val nearbyPlayers = ConcurrentHashMap<UUID, List<Player>>()

    fun startTasks() {
        PlayerCheckTask().runTaskTimer(wrappedScheduler, config.initialTaskTimer, config.playerCheckInterval)
        ActionBarUpdateTask().runTaskTimer(wrappedScheduler, config.initialTaskTimer, config.actionBarUpdateInterval)
    }

    fun getPlayerCount(uuid: UUID): Int = playerCounts.getOrDefault(uuid, 0)

    fun setPlayerCount(uuid: UUID, count: Int) {
        playerCounts[uuid] = count
    }

    fun getNearbyPlayers(player: Player): List<Player> = nearbyPlayers[player.uniqueId] ?: emptyList()

    fun setNearbyPlayers(player: Player, nearby: List<Player>) {
        nearbyPlayers[player.uniqueId] = nearby
    }
}