package clutchy.me.foundation

import clutchy.me.foundation.extensions.*

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.dependency.Libraries
import org.bukkit.plugin.java.annotation.dependency.Library
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.Website
import org.bukkit.plugin.java.annotation.plugin.author.Author

@Plugin(name = "Foundation", version = "1.0.0")
@Description("Core plugin that expands the Bukkit API")
@Author("Clutch")
@Website("https://clutchy.me")
@ApiVersion(ApiVersion.Target.v1_20)
@Libraries(value = [Library("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")])
class Foundation: JavaPlugin() {

    override fun onLoad() {

    }

    override fun onEnable() {
        "Info".info()
        "Warning".warning()
        "Error".error()
        "Debug".debug()
        addListener(PlayerListener())
    }

    override fun onDisable() {

    }
}
