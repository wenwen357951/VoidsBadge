import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    alias(libs.plugins.pluginyml)
    alias(libs.plugins.runpaper)
}

group = "com.wennest.yeemo"
version = "1.0.0-SNAPSHOT"
description = "This plugin is the Yeemo Voids server badge management system."

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "PlaceholderAPI"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    // Minecraft
    compileOnly(libs.papermc)

    // Hooks
    compileOnly(libs.placeholderapi)

    // Utils
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    library(libs.gson)
    bukkitLibrary(libs.gson)
}

tasks {
    runServer {
        minecraftVersion("1.20.1")
    }
}

bukkit {
    main = "${group}.vbadge.VBadge"
    apiVersion = "1.20"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    authors = listOf("WenWen_xD")
    depend = listOf("PlaceholderAPI")
}
