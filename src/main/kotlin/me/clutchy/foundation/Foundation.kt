package me.clutchy.foundation

import me.clutchy.foundation.extensions.plus
import me.clutchy.foundation.modules.ModuleHandler
import org.bukkit.ChatColor
import java.util.logging.Logger

class Foundation(classLoader: ClassLoader, private val logger: Logger) {

    private val modules = ModuleHandler(classLoader, logger)

    fun onEnable() {
        logger.info(ChatColor.GREEN + "Enabled Kotlin")
        modules.loadModules()
    }

    fun onDisable() {
        logger.info(ChatColor.RED + "Disabled Kotlin")
        modules.disableModules()
    }
}
