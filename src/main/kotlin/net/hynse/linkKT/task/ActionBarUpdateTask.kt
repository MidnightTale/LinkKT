//package net.hynse.linkKT.task
//
//import me.nahu.scheduler.wrapper.runnable.WrappedRunnable
//import net.hynse.linkKT.LinkKT.Companion.playerManager
//import net.hynse.linkKT.LinkKT.Companion.wrappedScheduler
//import net.hynse.linkKT.util.ActionBarUtil
//import org.bukkit.Bukkit
//
//class ActionBarUpdateTask : WrappedRunnable() {
//    override fun run() {
//        wrappedScheduler.runTaskAsynchronously {
//            for (player in Bukkit.getOnlinePlayers()) {
//                val uuid = player.uniqueId
//                val nearbyPlayerUUIDs = playerManager.getNearbyPlayers(uuid)
//                val nearbyPlayerCount = nearbyPlayerUUIDs.size
//                val actionBar = ActionBarUtil.createActionBarText(nearbyPlayerCount)
//
//                wrappedScheduler.runTask {
//                    player.sendActionBar(actionBar)
//                }
//            }
//        }
//    }
//}