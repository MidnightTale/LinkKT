package net.hynse.linkKT.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Represents a custom event for player respawn in Folia.
 *
 * This event is fired when a player respawns in a Folia-based server.
 * It extends the Bukkit Event class and is set to be asynchronous.
 *
 * @property player The player who is respawning.
 */
class PlayerRespawnEventFolia(val player: Player) : Event(true) {
    companion object {
        private val HANDLERS = HandlerList()

        /**
         * Gets the handler list for this event.
         *
         * This method is required by the Bukkit event system to register the event.
         *
         * @return The handler list for this event.
         */
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    /**
     * Gets the handler list for this event.
     *
     * This method is required by the Bukkit event system to register the event.
     *
     * @return The handler list for this event.
     */
    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}