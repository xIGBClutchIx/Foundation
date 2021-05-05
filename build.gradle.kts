import me.clutchy.dependenciesgen.gradle.DependenciesGen
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm") version "1.5.0-RC"
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("me.clutchy.dependenciesgen") version "1.0.1"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    api("me.clutchy:DependenciesGen:1.0.1")
    api("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    api("org.reflections:reflections:0.9.12")
}

configure<DependenciesGen> {
    ignored = listOf("me.clutchy:DependenciesGen", "com.destroystokyo.paper:paper-api")
}

tasks.withType<ShadowJar> {
    exclude("/me/clutchy/dependenciesgen/gradle/")
    dependencies {
        include(dependency("me.clutchy:DependenciesGen:1.0.1"))
    }
}

// Default tasks for DependenciesGen
tasks.getByName("classes").dependsOn(tasks.getByName("gen-dependencies"))
tasks.getByName("build").dependsOn(tasks.getByName("shadowJar"))

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

group = "me.clutchy"
version = "0.0.1"
