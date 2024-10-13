package net.hynse.linkKT.task

import me.nahu.scheduler.wrapper.runnable.WrappedRunnable
import net.hynse.linkKT.LinkKT.Companion.playerManager
import net.hynse.linkKT.util.ActionBarUtil
import org.bukkit.Bukkit

class ActionBarUpdateTask : WrappedRunnable() {
    override fun run() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val nearbyPlayers = playerManager.getNearbyPlayers(player)
            val nearbyPlayerCount = nearbyPlayers.size
            ActionBarUtil.updateActionBar(player, nearbyPlayers, nearbyPlayerCount)
        }
    }
}