package net.hynse.linkKT

import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin
import me.nahu.scheduler.wrapper.WrappedScheduler
import net.hynse.linkKT.api.RespawnAPI
import net.hynse.linkKT.config.Config
import net.hynse.linkKT.manager.BoostManager
import net.hynse.linkKT.manager.PlayerManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit

class LinkKT : FoliaWrappedJavaPlugin() {

    companion object {
        lateinit var instance: LinkKT
            private set
        val wrappedScheduler: WrappedScheduler by lazy { instance.scheduler }
        val playerManager: PlayerManager by lazy { instance.playerManager }
        val boostManager: BoostManager by lazy { instance.boostManager }
        val config: Config by lazy { instance.config }
        val respawnAPI: RespawnAPI by lazy { instance.respawnAPI }
    }

    lateinit var playerManager: PlayerManager
        private set
    lateinit var boostManager: BoostManager
        private set
    lateinit var config: Config
        private set
    lateinit var respawnAPI: RespawnAPI
        private set


    private object Colors {
        val BORDER = TextColor.color(0xFFD700)  // Gold
        val HEADER = TextColor.color(0x00BFFF)  // Deep Sky Blue
        val COMPONENT = TextColor.color(0xFFFFFF)  // Lime Green
        val SUCCESS = TextColor.color(0x00FF00)  // Green
        val FAILURE = TextColor.color(0xFF4500)  // Orange Red
        val READY = TextColor.color(0x7CFC00)  // Lawn Green
    }

    override fun onEnable() {
        instance = this

        logHeader("LinkKT")
        printPluginInfo()

        try {
            logStep("Configuration") {
                config = Config()
            }

            logStep("PlayerManager") {
                playerManager = PlayerManager()
            }

            logStep("BoostManager") {
                boostManager = BoostManager()
            }

            logStep("PlayerManager Tasks") {
                playerManager.startTasks()
            }

            logStep("RespawnAPI") {
                respawnAPI = RespawnAPI()
                server.pluginManager.registerEvents(respawnAPI, this)
            }

            logFooter("LinkKT is ready!")
        } catch (e: Exception) {
            logError("An error occurred during initialization", e)
            server.pluginManager.disablePlugin(this)
        }
    }

    private fun logStep(component: String, task: () -> Unit) {
        try {
            task()
            componentLogger.info(Component.text("│ ").color(Colors.BORDER)
                .append(Component.text("✔ ").color(Colors.SUCCESS))
                .append(Component.text("$component: ").color(Colors.COMPONENT))
                .append(Component.text("Initialized").color(Colors.SUCCESS)))
        } catch (e: Exception) {
            componentLogger.info(Component.text("│ ").color(Colors.BORDER)
                .append(Component.text("✖ ").color(Colors.FAILURE))
                .append(Component.text("$component: ").color(Colors.COMPONENT))
                .append(Component.text("Failed").color(Colors.FAILURE)))
            throw e  // Re-throw the exception to be caught in onEnable
        }
    }

    private fun logError(message: String, e: Exception) {
        componentLogger.error(Component.text("┌─────────────────────────────────────┐").color(Colors.FAILURE))
        componentLogger.error(Component.text("│ ERROR: $message").color(Colors.FAILURE))
        componentLogger.error(Component.text("├─────────────────────────────────────┤").color(Colors.FAILURE))
        componentLogger.error(Component.text("│ ${e.message}").color(Colors.FAILURE))
        componentLogger.error(Component.text("└─────────────────────────────────────┘").color(Colors.FAILURE))
        e.printStackTrace()
    }

    private fun logHeader(message: String) {
        componentLogger.info(Component.text("┌─────────────────────────────────────┐").color(Colors.BORDER))
        componentLogger.info(Component.text("│ $message").color(Colors.HEADER))
        componentLogger.info(Component.text("├─────────────────────────────────────┤").color(Colors.BORDER))
    }

    private fun logFooter(message: String) {
        componentLogger.info(Component.text("├─────────────────────────────────────┤").color(Colors.BORDER))
        componentLogger.info(Component.text("│ $message").color(Colors.READY))
        componentLogger.info(Component.text("└─────────────────────────────────────┘").color(Colors.BORDER))
    }

    private fun printPluginInfo() {
        val pluginVersion = pluginMeta.version
        val serverVersion = server.version
        val bukkitVersion = Bukkit.getBukkitVersion()
        val javaVersion = System.getProperty("java.version")
        val serverSoftware = when {
            server.name.contains("Paper", ignoreCase = true) -> "Paper"
            server.name.contains("Spigot", ignoreCase = true) -> "Spigot"
            server.name.contains("Folia", ignoreCase = true) -> "Folia"
            else -> server.name
        }

        componentLogger.info(Component.text("│ ").color(Colors.BORDER)
            .append(Component.text("Plugin Version: ").color(Colors.COMPONENT))
            .append(Component.text(pluginVersion).color(Colors.SUCCESS)))
        componentLogger.info(Component.text("│ ").color(Colors.BORDER)
            .append(Component.text("Server Software: ").color(Colors.COMPONENT))
            .append(Component.text(serverSoftware).color(Colors.SUCCESS)))
        componentLogger.info(Component.text("│ ").color(Colors.BORDER)
            .append(Component.text("Server Version: ").color(Colors.COMPONENT))
            .append(Component.text(serverVersion).color(Colors.SUCCESS)))
        componentLogger.info(Component.text("│ ").color(Colors.BORDER)
            .append(Component.text("Bukkit Version: ").color(Colors.COMPONENT))
            .append(Component.text(bukkitVersion).color(Colors.SUCCESS)))
        componentLogger.info(Component.text("│ ").color(Colors.BORDER)
            .append(Component.text("Java Version: ").color(Colors.COMPONENT))
            .append(Component.text(javaVersion).color(Colors.SUCCESS)))
        componentLogger.info(Component.text("│ ").color(Colors.BORDER)
            .append(Component.text("Data Folder: ").color(Colors.COMPONENT))
            .append(Component.text(dataFolder.absolutePath).color(Colors.SUCCESS)))
        componentLogger.info(Component.text("├─────────────────────────────────────┤").color(Colors.BORDER))
    }
}