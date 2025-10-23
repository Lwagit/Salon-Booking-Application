// Top-level build file for configuration common to all modules
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.0.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

// No repositories block here — they should be in settings.gradle.kts
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
