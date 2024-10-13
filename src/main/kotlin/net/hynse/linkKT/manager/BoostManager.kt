package net.hynse.linkKT.manager

import net.hynse.linkKT.LinkKT.Companion.instance
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.NamespacedKey

class BoostManager() {
    private val boostKeys = mapOf(
        Attribute.GENERIC_MOVEMENT_SPEED to listOf(
            NamespacedKey(instance, "speed_boost_tier_1"),
            NamespacedKey(instance, "speed_boost_tier_2"),
            NamespacedKey(instance, "speed_boost_tier_3")
        ),
        Attribute.GENERIC_LUCK to listOf(
            NamespacedKey(instance, "luck_boost_tier_1"),
            NamespacedKey(instance, "luck_boost_tier_2"),
            NamespacedKey(instance, "luck_boost_tier_3")
        ),
        Attribute.GENERIC_ATTACK_DAMAGE to listOf(
            NamespacedKey(instance, "attack_damage_boost_tier_1"),
            NamespacedKey(instance, "attack_damage_boost_tier_2"),
            NamespacedKey(instance, "attack_damage_boost_tier_3")
        ),
        Attribute.GENERIC_ARMOR to listOf(
            NamespacedKey(instance, "armor_boost_tier_1"),
            NamespacedKey(instance, "armor_boost_tier_2"),
            NamespacedKey(instance, "armor_boost_tier_3")
        )
    )

    fun applyBoost(player: Player, nearbyPlayerCount: Int) {
        when (nearbyPlayerCount) {
            1 -> {
                applyBoostTier(player, Attribute.GENERIC_MOVEMENT_SPEED, 0, 0.1)  // 10% speed increase
                applyBoostTier(player, Attribute.GENERIC_LUCK, 0, 0.5)            // +0.5 luck
                applyBoostTier(player, Attribute.GENERIC_ATTACK_DAMAGE, 0, 0.05)  // 5% attack damage increase
                applyBoostTier(player, Attribute.GENERIC_ARMOR, 0, 1.0)           // +1 armor
            }
            2 -> {
                applyBoostTier(player, Attribute.GENERIC_MOVEMENT_SPEED, 1, 0.2)  // 20% speed increase
                applyBoostTier(player, Attribute.GENERIC_LUCK, 1, 1.0)            // +1.0 luck
                applyBoostTier(player, Attribute.GENERIC_ATTACK_DAMAGE, 1, 0.1)   // 10% attack damage increase
                applyBoostTier(player, Attribute.GENERIC_ARMOR, 1, 2.0)           // +2 armor
            }
            3 -> {
                applyBoostTier(player, Attribute.GENERIC_MOVEMENT_SPEED, 2, 0.3)  // 30% speed increase
                applyBoostTier(player, Attribute.GENERIC_LUCK, 2, 1.5)            // +1.5 luck
                applyBoostTier(player, Attribute.GENERIC_ATTACK_DAMAGE, 2, 0.15)  // 15% attack damage increase
                applyBoostTier(player, Attribute.GENERIC_ARMOR, 2, 3.0)           // +3 armor
            }
            else -> removeAllBoosts(player)
        }
    }
    /**
     * Applies a specific tier of boost to a player's attribute.
     *
     * @param player The player to whom the boost will be applied.
     * @param attribute The attribute to be boosted.
     * @param tier The tier of the boost, used to determine which NamespacedKey to use.
     * @param boost The value of the boost to be applied.
     */
    private fun applyBoostTier(player: Player, attribute: Attribute, tier: Int, boost: Double) {
        player.getAttribute(attribute)?.let { attributeInstance ->
            removeBoostsForAttribute(player, attribute)

            val modifier = AttributeModifier(
                boostKeys[attribute]!![tier],
                boost,
                when (attribute) {
                    Attribute.GENERIC_MOVEMENT_SPEED, Attribute.GENERIC_ATTACK_DAMAGE -> AttributeModifier.Operation.MULTIPLY_SCALAR_1
                    else -> AttributeModifier.Operation.ADD_NUMBER
                }
            )
            attributeInstance.addModifier(modifier)
            instance.logger.info("Applied Tier ${tier + 1} ${attribute.name.lowercase()} boost (${formatBoostValue(attribute, boost)}) to player ${player.name}")
        } ?: run {
            instance.logger.warning("Failed to apply Tier ${tier + 1} ${attribute.name.lowercase()} boost to player ${player.name}: Attribute not found")
        }
    }

    private fun formatBoostValue(attribute: Attribute, boost: Double): String {
        return when (attribute) {
            Attribute.GENERIC_MOVEMENT_SPEED, Attribute.GENERIC_ATTACK_DAMAGE -> "${boost * 100}%"
            else -> "+$boost"
        }
    }

    fun removeAllBoosts(player: Player) {
        boostKeys.forEach { (attribute, keys) ->
            player.getAttribute(attribute)?.let { attributeInstance ->
                val removedModifiers = attributeInstance.modifiers
                    .filter { it.key in keys }
                    .onEach { attributeInstance.removeModifier(it) }

                if (removedModifiers.isNotEmpty()) {
                    instance.logger.info("Removed ${removedModifiers.size} ${attribute.name.lowercase()} boost(s) from player ${player.name}")
                }
            }
        }
    }

    private fun removeBoostsForAttribute(player: Player, attribute: Attribute) {
        player.getAttribute(attribute)?.let { attributeInstance ->
            val keys = boostKeys[attribute] ?: return
            attributeInstance.modifiers
                .filter { it.key in keys }
                .forEach { attributeInstance.removeModifier(it) }
        }
    }
}