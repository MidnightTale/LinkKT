package net.hynse.linkKT.config

import net.hynse.linkKT.LinkKT.Companion.instance
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config() {
    private val configFile: File = File(instance.dataFolder, "config.yml")
    private var config: FileConfiguration = YamlConfiguration()

    val initialTaskTimer: Long
    val playerCheckInterval: Long
    val actionBarUpdateInterval: Long
    val tiers: Map<Int, TierConfig>

    init {
        if (!configFile.exists()) {
            instance.saveResource("config.yml", false)
        }
        config.load(configFile)

        initialTaskTimer = config.getLong("initialTaskTimer", 20L)
        playerCheckInterval = config.getLong("playerCheckInterval", 20L)
        actionBarUpdateInterval = config.getLong("actionBarUpdateInterval", 20L)
        tiers = loadTiers()
    }

    private fun loadTiers(): Map<Int, TierConfig> {
        val tiersMap = mutableMapOf<Int, TierConfig>()
        val tiersSection = config.getConfigurationSection("tiers") ?: return emptyMap()

        for (tierKey in tiersSection.getKeys(false)) {
            val tierNumber = tierKey.removePrefix("tier").toIntOrNull() ?: continue
            val tierSection = tiersSection.getConfigurationSection(tierKey) ?: continue

            val playersNearby = tierSection.getInt("playersNearby")
            val boosts = mutableMapOf<String, BoostConfig>()

            val boostsSection = tierSection.getConfigurationSection("boosts") ?: continue
            for (boostKey in boostsSection.getKeys(false)) {
                val boostSection = boostsSection.getConfigurationSection(boostKey) ?: continue
                boosts[boostKey] = BoostConfig(
                    mode = boostSection.getString("mode") ?: "attribute",
                    attribute = boostSection.getString("attribute"),
                    value = boostSection.getDouble("value"),
                    type = boostSection.getString("type") ?: "additive"
                )
            }

            tiersMap[tierNumber] = TierConfig(playersNearby, boosts)
        }

        return tiersMap
    }

    data class TierConfig(val playersNearby: Int, val boosts: Map<String, BoostConfig>)

    data class BoostConfig(
        val mode: String,
        val attribute: String?,
        val value: Double,
        val type: String
    )
}