import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm") version "1.4.10"
}

repositories {
    mavenLocal()
    jcenter()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
    implementation("org.reflections:reflections:0.9.12")
}

// Java 8
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

// Compile java and kotlin
sourceSets.main {
    withConvention(KotlinSourceSet::class) {
        java.srcDirs()
        kotlin.srcDirs("src/main/java", "src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
}

group = "me.clutchy.foundation"
version = "0.0.1"
