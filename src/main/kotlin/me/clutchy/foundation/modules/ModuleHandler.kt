package me.clutchy.foundation.modules

import me.clutchy.dependenciesgen.downloader.DependencyDownloader
import me.clutchy.dependenciesgen.downloader.LoadJarsUtil
import me.clutchy.foundation.extensions.plus
import org.bukkit.ChatColor
import org.reflections.Reflections
import java.io.File
import java.nio.file.Path
import java.util.logging.Logger

class ModuleHandler(private val classLoader: ClassLoader, private val logger: Logger) {

    private var modules: MutableMap<String, Module> = mutableMapOf()
    private val modulesFolder: Path = Path.of("modules")

    fun loadModules() {
        logger.info(ChatColor.GREEN + "Loading modules...")
        // Disable reflections logging
        Reflections.log = null
        // Create modules folder
        modulesFolder.toFile().mkdirs()
        // Search and load all jars
        var moduleNames = emptyArray<String>()
        modulesFolder.toFile().walk().filter { it.isFile }.filter { it.extension == "jar" }.forEach {
            moduleNames += it.nameWithoutExtension
            LoadJarsUtil.addFile(classLoader, logger, it)
        }
        // Log all modules
        if (moduleNames.isNotEmpty()) {
            logger.info("Loaded modules: " + ChatColor.DARK_PURPLE + moduleNames.sortedArray().joinToString(ChatColor.RESET.toString() + ", " + ChatColor.DARK_PURPLE))
        } else {
            logger.warning("No modules to load!")
        }
        // Find all the module classes
        val moduleClasses: Set<Class<out Module?>> = Reflections(classLoader).getSubTypesOf(Module::class.java)
        // Create and enable all modules
        moduleClasses.mapNotNull { moduleClass -> moduleClass.getConstructor().newInstance() }.forEach { module ->
            module.logger.info(ChatColor.GREEN + "Enabling...")
            val dependencyDownloader = DependencyDownloader(classLoader, module.logger)
            dependencyDownloader.downloadDependencies(module.javaClass.getResourceAsStream("META-INF" + File.separator + "dependencies.json"))
            module.onEnable()
            module.logger.info(ChatColor.GREEN + "Enabled!")
            modules[module.name] = module
        }
    }

    fun disableModules() = modules.keys.forEach { disableModule(it) }

    @Suppress("MemberVisibilityCanBePrivate")
    fun disableModule(moduleName: String) {
        val module = modules[moduleName]
        if (module != null) {
            module.logger.info(ChatColor.YELLOW + "Disabling...")
            module.onDisable()
            module.logger.info(ChatColor.RED + "Disabled!")
            modules.remove(moduleName)
        }
    }
}
