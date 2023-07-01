package clutchy.me.foundation.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit

private fun String.log(pluginName: String, color: NamedTextColor) {
    val prefix = Component.text(pluginName, NamedTextColor.DARK_AQUA)
    val separator = Component.text(" > ", NamedTextColor.DARK_GRAY)
    val message = Component.text(this, color)
    Bukkit.getConsoleSender().sendMessage(Component.empty().append(prefix).append(separator).append(message))
}

fun String.info(pluginName: String = "Foundation") = log(pluginName, NamedTextColor.GREEN)
fun String.warning(pluginName: String = "Foundation") = log(pluginName, NamedTextColor.GOLD)
fun String.error(pluginName: String = "Foundation") = log(pluginName, NamedTextColor.RED)
fun String.debug(pluginName: String = "Foundation") = log(pluginName, NamedTextColor.DARK_PURPLE)
