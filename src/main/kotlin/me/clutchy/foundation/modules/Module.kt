package me.clutchy.foundation.modules

import java.util.logging.Logger

class Module(val name: String, val version: String) {

    val logger: Logger = Logger.getLogger(name)

    fun onEnable() {

    }

    fun onDisable() {

    }
}
