plugins {
    kotlin("jvm") version "1.4.0"
}

repositories {
    mavenLocal()
    jcenter()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.3-R0.1-SNAPSHOT")
}

group = "me.clutchy.foundation"
version = "0.0.1"
