package me.clutchy.foundation.modules

import org.bukkit.ChatColor
import org.reflections.Reflections
import java.nio.file.Path
import java.util.logging.Logger

class ModuleHandler(private val classLoader: ClassLoader, private val logger: Logger) {

    private var modules: MutableMap<String, Module> = mutableMapOf()
    private val modulesFolder: Path = Path.of("modules")

    fun loadModules() {
        // Create modules folder
        modulesFolder.toFile().mkdirs()
        // Search and load all jars
        var moduleNames = emptyArray<String>()
        modulesFolder.toFile().walk().filter { it.isFile }.filter { it.extension == "jar" }.forEach {
            moduleNames += it.nameWithoutExtension
            //LoadJarsUtil.addFile(classLoader, logger, it)
        }
        // Log all modules
        if (moduleNames.isNotEmpty()) logger.info("Loaded modules: " + ChatColor.DARK_PURPLE + moduleNames.sortedArray().joinToString(ChatColor.RESET.toString() + ", " + ChatColor.DARK_PURPLE))
        // Find all the module classes
        val moduleClasses: Set<Class<out Module?>> = Reflections(classLoader).getSubTypesOf(Module::class.java)
        // Create and enable all modules
        moduleClasses.mapNotNull { moduleClass -> moduleClass.getConstructor().newInstance() }.forEach { module ->
            logger.info("Enabling module: " + ChatColor.YELLOW + module.name)
            module.onEnable()
            logger.info("Enabled module: " + ChatColor.GREEN + module.name)
            modules[module.name] = module
        }
    }

    fun disableModules() = modules.keys.forEach { disableModule(it) }

    @Suppress("MemberVisibilityCanBePrivate")
    fun disableModule(moduleName: String) {
        val module = modules[moduleName]
        if (module != null) {
            logger.info("Disabling module: " + ChatColor.YELLOW + module.name)
            module.onDisable()
            logger.info("Disabled module: " + ChatColor.RED + module.name)
        }
        modules.remove(moduleName)
    }
}
