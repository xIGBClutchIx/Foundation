package clutchy.me.foundation.extensions

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import java.util.*

data class PlayerData(val balance: Int = 0) {

    companion object: Listener {

        private val data = HashMap<UUID, PlayerData>()

        @EventHandler(priority = EventPriority.LOWEST)
        private fun onLogin(e: AsyncPlayerPreLoginEvent) {
            // Load data - TODO
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private fun onLogin(e: PlayerLoginEvent) {
            // Load data - TODO
            e.player.onLogin()
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private fun onJoin(e: PlayerJoinEvent) {
            e.player.onJoin()
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private fun onPlayerQuit(event: PlayerQuitEvent) {
            event.player.onLeave(false)
            data.remove(event.player.uniqueId)
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private fun onPlayerKick(event: PlayerKickEvent) {
            event.player.onLeave(true)
            data.remove(event.player.uniqueId)
        }

        internal fun getData(uuid: UUID): PlayerData {
            return data[uuid]!!
        }
    }
}

val Player.data: PlayerData
    get() = PlayerData.getData(uniqueId)

fun Player.onLogin() {
    "$name logging in POG!".debug()
}

fun Player.onJoin() {
    "$name joined POG!".debug()
}

fun Player.onLeave(kicked: Boolean) {
    if (kicked) {
        "$name got kicked SADGE!".debug()
    } else {
        "$name left SADGE!".debug()
    }
}
