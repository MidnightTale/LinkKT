package net.hynse.linkKT.manager

import net.hynse.linkKT.LinkKT.Companion.config
import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler
import net.hynse.linkKT.task.PlayerCheckTask
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class PlayerManager {
    private val playerCount: MutableMap<UUID, Int> = ConcurrentHashMap()
    private val nearbyPlayers: MutableMap<UUID, List<UUID>> = ConcurrentHashMap()
    fun startTasks() {
        PlayerCheckTask().runTaskTimerAsynchronously(
            wrappedScheduler,
            config.initialTaskTimer,
            config.playerCheckInterval
        )
//        ActionBarUpdateTask().runTaskTimerAsynchronously(
//            wrappedScheduler,
//            config.initialTaskTimer,
//            config.actionBarUpdateInterval
//        )
    }

    fun getPlayerCount(uuid: UUID): Int = playerCount[uuid] ?: 0

    fun setPlayerCount(uuid: UUID, count: Int) {
        playerCount[uuid] = count
    }

    fun getNearbyPlayers(uuid: UUID): List<UUID> {
        return nearbyPlayers[uuid] ?: emptyList()
    }

    fun setNearbyPlayers(uuid: UUID, nearby: List<UUID>) {
        nearbyPlayers[uuid] = nearby
    }
    fun clearData() {
        playerCount.clear()
        nearbyPlayers.clear()
    }

}