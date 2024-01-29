// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {   // Add this
    extra.apply {
        set("room_version", "2.6.1")
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.0.2" apply false  // Add this if it's not available
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}

tasks.register("Clean", Delete::class) { // Better practice to add this code snippet here
    delete(rootProject.buildDir)
}