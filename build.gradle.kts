// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.library") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath(kotlin("gradle-plugin", version = "1.9.3"))
        classpath("com.google.gms:google-services:4.4.1")

    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

}


