package net.hynse.linkKT.manager

import net.hynse.linkKT.LinkKT.Companion.instance
import net.hynse.linkKT.LinkKT.Companion.config
import net.hynse.linkKT.config.Config
import org.bukkit.attribute.AttributeModifier
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.Particle

class BoostManager {
    private val boostPrefix = "linkKT_boost_"

    fun applyBoost(player: Player, nearbyPlayerCount: Int) {
        val tier = config.tiers.entries.firstOrNull { it.value.playersNearby == nearbyPlayerCount }
        if (tier != null) {
            removeAllBoosts(player)
            applyTierBoosts(player, tier.key, tier.value)
            spawnTierParticles(player, tier.key)
        } else {
            removeAllBoosts(player)
        }
    }

    private fun applyTierBoosts(player: Player, tierNumber: Int, tierConfig: Config.TierConfig) {
        tierConfig.boosts.forEach { (boostName, boostConfig) ->
            when (boostConfig.mode) {
                "attribute" -> applyAttributeBoost(player, tierNumber, boostName, boostConfig)
                // Add other boost modes here if needed
            }
        }
    }

    private fun applyAttributeBoost(player: Player, tierNumber: Int, boostName: String, boostConfig: Config.BoostConfig) {
        val attribute = Attribute.valueOf(boostConfig.attribute ?: return)
        player.getAttribute(attribute)?.let { attributeInstance ->
            val key = NamespacedKey(instance, "$boostPrefix${boostName}_tier_$tierNumber")
            val operation = when (boostConfig.type) {
                "percentage" -> AttributeModifier.Operation.MULTIPLY_SCALAR_1
                else -> AttributeModifier.Operation.ADD_NUMBER
            }
            attributeInstance.addModifier(AttributeModifier(key, boostConfig.value, operation))
        }
    }

    fun removeAllBoosts(player: Player) {
        Attribute.entries.forEach { attribute ->
            player.getAttribute(attribute)?.let { attributeInstance ->
                attributeInstance.modifiers
                    .filter { it.name.startsWith(boostPrefix) }
                    .forEach { attributeInstance.removeModifier(it) }
            }
        }
    }

    private fun spawnTierParticles(player: Player, tier: Int) {
        val world = player.world
        val location = player.location.add(0.0, 1.0, 0.0)
        when (tier) {
            1 -> world.spawnParticle(Particle.HAPPY_VILLAGER, location, 10, 0.5, 0.5, 0.5, 0.1)
            2 -> world.spawnParticle(Particle.TRIAL_OMEN, location, 15, 0.5, 0.5, 0.5, 0.1)
            3 -> world.spawnParticle(Particle.END_ROD, location, 20, 0.5, 0.5, 0.5, 0.1)
        }
    }
}