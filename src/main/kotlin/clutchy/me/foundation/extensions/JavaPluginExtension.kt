package clutchy.me.foundation.extensions

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass

val <T: JavaPlugin> KClass<T>.instance
    get() = JavaPlugin.getPlugin(this.java)

fun JavaPlugin.addListener(listener: Listener) = server.pluginManager.registerEvents(listener, this)
