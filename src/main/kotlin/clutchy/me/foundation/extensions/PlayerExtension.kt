package clutchy.me.foundation.extensions

import org.bukkit.entity.Player

fun Player.onJoin() {
    "$name joined POG!".debug()
}

fun Player.onLeave(kicked: Boolean) {

}
