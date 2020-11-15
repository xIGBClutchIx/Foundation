package me.clutchy.foundation

import me.clutchy.foundation.extensions.plus
import me.clutchy.foundation.modules.ModuleHandler
import org.bukkit.ChatColor

class Foundation(private val foundation: FoundationStartup) {

    private val modules = ModuleHandler(foundation.javaClass.classLoader, foundation.logger)

    fun onEnable() {
        foundation.logger.info(ChatColor.GREEN + "Enabled Kotlin")
        modules.loadModules()
    }

    fun onDisable() {
        foundation.logger.info(ChatColor.RED + "Disabled Kotlin")
        modules.disableModules()
    }
}
