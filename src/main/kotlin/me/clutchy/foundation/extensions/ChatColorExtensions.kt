package me.clutchy.foundation.extensions

import org.bukkit.ChatColor

operator fun ChatColor.plus(string: String): String = toString() + string
operator fun ChatColor.plus(color: ChatColor): String = toString() + color
