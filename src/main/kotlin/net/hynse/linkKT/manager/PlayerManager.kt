package net.hynse.linkKT.manager

import net.hynse.linkKT.LinkKT.Companion.config
import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler
import net.hynse.linkKT.task.PlayerCheckTask
import net.hynse.linkKT.task.ActionBarUpdateTask
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerManager() {
    private val playerCount: MutableMap<UUID, Int> = ConcurrentHashMap()
    private val nearbyPlayers: MutableMap<UUID, List<Player>> = ConcurrentHashMap()

    fun startTasks() {
        PlayerCheckTask().runTaskTimer(wrappedScheduler, config.initialTaskTimer , config.playerCheckInterval)
        ActionBarUpdateTask().runTaskTimer(wrappedScheduler, config.initialTaskTimer, config.actionBarUpdateInterval)
    }

    fun getPlayerCount(uuid: UUID): Int = playerCount[uuid] ?: 0

    fun setPlayerCount(uuid: UUID, count: Int) {
        playerCount[uuid] = count
    }

    fun setNearbyPlayers(player: Player, nearby: List<Player>) {
        nearbyPlayers[player.uniqueId] = nearby
    }

    fun getNearbyPlayers(player: Player): List<Player> = nearbyPlayers[player.uniqueId] ?: emptyList()
}