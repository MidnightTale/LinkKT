package net.hynse.linkKT.util

import net.hynse.linkKT.LinkKT.Companion.boostManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import kotlin.math.abs

object ActionBarUtil {

    fun createActionBarText(nearbyPlayerCount: Int): Component {
        val boosts = boostManager.getActiveBoosts(nearbyPlayerCount)

        return Component.text()
            .append(createBoostComponent(boosts, nearbyPlayerCount))
            .build()
    }

    private fun createBoostComponent(boosts: List<String>, nearbyPlayerCount: Int): Component {
        if (boosts.isEmpty() || nearbyPlayerCount == 0) {
            return Component.text("No nearby players")
                .color(TextColor.color(255, 255, 255))
        }
        return Component.text()
            .append(
                boosts.mapIndexed { index, boost ->
                    Component.text()
                        .append(Component.text(boost, getBoostColor(index)))
                        .append(Component.text(" "))
                        .build()
                }.reduce { acc, component -> acc.append(component) }
            )
            .build()
    }

    private fun getBoostColor(index: Int): TextColor {
        val hue = (index * 137.5f) % 360f
        return TextColor.color(hsvToRgb(hue, 1.0f, 1.0f))
    }

    private fun hsvToRgb(h: Float, s: Float, v: Float): Int {
        val c = v * s
        val x = c * (1 - abs((h / 60f) % 2 - 1))
        val m = v - c
        val (r, g, b) = when ((h / 60f).toInt()) {
            0 -> Triple(c, x, 0f)
            1 -> Triple(x, c, 0f)
            2 -> Triple(0f, c, x)
            3 -> Triple(0f, x, c)
            4 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }
        return (((r + m) * 255).toInt() shl 16) or
                (((g + m) * 255).toInt() shl 8) or
                ((b + m) * 255).toInt()
    }
}