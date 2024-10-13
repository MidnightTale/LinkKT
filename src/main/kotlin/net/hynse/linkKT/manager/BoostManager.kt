package net.hynse.linkKT.manager

import net.hynse.linkKT.LinkKT.Companion.instance
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class BoostManager {
    private val attributes = mapOf(
        Attribute.GENERIC_MOVEMENT_SPEED to AttributeInfo("speed", "\uD83D\uDDF2", "%", 2),
        Attribute.GENERIC_LUCK to AttributeInfo("luck", "\uD83C\uDF40", "", 2),
        Attribute.GENERIC_ATTACK_DAMAGE to AttributeInfo("attack_damage", "\uD83D\uDDE1", "%", 2),
        Attribute.GENERIC_ARMOR to AttributeInfo("armor", "\uD83D\uDEE1", "", 1),
        Attribute.GENERIC_MOVEMENT_EFFICIENCY to AttributeInfo("movement_efficiency", "\uD83D\uDC5F", "%", 2),
        Attribute.GENERIC_OXYGEN_BONUS to AttributeInfo("oxygen_bonus", "\uD83C\uDF0A", "", 1),
        Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY to AttributeInfo("water_movement", "\uD83C\uDF0A", "%", 2),
        Attribute.PLAYER_BLOCK_BREAK_SPEED to AttributeInfo("block_break_speed", "\uD83D\uDD28", "%", 2),
        Attribute.PLAYER_MINING_EFFICIENCY to AttributeInfo("mining_efficiency", "\u26CF", "%", 2),
        Attribute.PLAYER_SNEAKING_SPEED to AttributeInfo("sneaking_speed", "\uD83D\uDEB6", "%", 2),
        Attribute.PLAYER_SUBMERGED_MINING_SPEED to AttributeInfo("submerged_mining", "\uD83C\uDF0A\u26CF", "%", 2)
    )

    private val boostKeys = attributes.mapValues { (_, info) ->
        (1..5).map { tier -> NamespacedKey(instance, "${info.name}_$tier") }
    }

    private val boostTiers: List<Map<Attribute, Double>> = listOf(
        createBoostArray(0.03, 0.1, 0.02, 0.00, 0.03, 0.0, 0.03, 0.02, 0.02, 0.03, 0.02), // Tier 1
        createBoostArray(0.06, 0.2, 0.04, 0.25, 0.04, 0.0, 0.06, 0.04, 0.04, 0.06, 0.04), // Tier 2
        createBoostArray(0.09, 0.3, 0.06, 0.50, 0.06, 0.0, 0.09, 0.06, 0.06, 0.09, 0.06), // Tier 3
        createBoostArray(0.12, 0.4, 0.08, 0.75, 0.08, 0.0, 0.12, 0.08, 0.08, 0.12, 0.08), // Tier 4
        createBoostArray(0.15, 0.5, 0.10, 1.00, 0.10, 0.0, 0.15, 0.10, 0.10, 0.15, 0.10)  // Tier 5
    ).map { values ->
        attributes.keys.zip(values.toList()).toMap()
    }
    fun applyBoost(player: Player, nearbyPlayerCount: Int) {
        removeAllBoosts(player)
        val tier = nearbyPlayerCount.coerceIn(1, boostTiers.size) - 1
        attributes.forEach { (attribute) ->
            val boost = boostTiers[tier][attribute] ?: return@forEach
            if (boost > 0.0) {
                player.getAttribute(attribute)?.addModifier(createModifier(attribute, tier, boost))
            }
        }
        spawnParticles(player, nearbyPlayerCount)
    }

    fun removeAllBoosts(player: Player) {
        boostKeys.forEach { (attribute, keys) ->
            player.getAttribute(attribute)?.let { attributeInstance ->
                keys.forEach { key ->
                    attributeInstance.removeModifier(key)
                }
            }
        }
    }

    fun spawnParticles(player: Player, count: Int) {
        if (count > 0) {
            player.world.spawnParticle(
                Particle.HAPPY_VILLAGER,
                player.location.add(0.0, 1.5, 0.0),
                count.coerceAtMost(5),
                0.5, 0.5, 0.5, 0.0
            )
        }
    }

    fun getActiveBoosts(nearbyPlayerCount: Int): List<String> {
        val tier = nearbyPlayerCount.coerceIn(1, boostTiers.size) - 1
        val excludedAttributes = setOf(
            Attribute.PLAYER_SUBMERGED_MINING_SPEED,
            Attribute.PLAYER_SNEAKING_SPEED,
            Attribute.PLAYER_MINING_EFFICIENCY,
            Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY,
            Attribute.GENERIC_OXYGEN_BONUS,
            Attribute.GENERIC_MOVEMENT_EFFICIENCY
        )

        return attributes
            .filter { (attribute, _) -> attribute !in excludedAttributes }
            .mapNotNull { (attribute, info) ->
                val boost = boostTiers[tier][attribute] ?: return@mapNotNull null
                if (boost > 0.0) {
                    val value = when {
                        info.suffix == "%" -> (boost * 100).toInt()
                        else -> String.format("%.${info.decimalPlaces}f", boost)
                    }
                    "${info.icon} +$value${info.suffix}"
                } else {
                    null
                }
            }
    }

    fun createBoostArray(
        movementSpeed: Double,
        luck: Double,
        attackDamage: Double,
        armor: Double,
        movementEfficiency: Double,
        oxygenBonus: Double,
        waterMovement: Double,
        blockBreakSpeed: Double,
        miningEfficiency: Double,
        sneakingSpeed: Double,
        submergedMiningSpeed: Double
    ): DoubleArray {
        return doubleArrayOf(
            movementSpeed,
            luck,
            attackDamage,
            armor,
            movementEfficiency,
            oxygenBonus,
            waterMovement,
            blockBreakSpeed,
            miningEfficiency,
            sneakingSpeed,
            submergedMiningSpeed
        )
    }


    private fun createModifier(attribute: Attribute, tier: Int, boost: Double): AttributeModifier {
        val operation = when (attribute) {
            Attribute.GENERIC_MOVEMENT_SPEED,
            Attribute.GENERIC_LUCK,
            Attribute.GENERIC_ATTACK_DAMAGE,
            Attribute.GENERIC_ARMOR,
            Attribute.GENERIC_MOVEMENT_EFFICIENCY,
            Attribute.GENERIC_OXYGEN_BONUS,
            Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY,
            Attribute.PLAYER_BLOCK_BREAK_SPEED,
            Attribute.PLAYER_MINING_EFFICIENCY,
            Attribute.PLAYER_SNEAKING_SPEED,
            Attribute.PLAYER_SUBMERGED_MINING_SPEED -> AttributeModifier.Operation.MULTIPLY_SCALAR_1
            else -> AttributeModifier.Operation.ADD_NUMBER
        }
        return AttributeModifier(boostKeys[attribute]!![tier], boost, operation)
    }

    private data class AttributeInfo(val name: String, val icon: String, val suffix: String, val decimalPlaces: Int)
}
