// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.library") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    alias(libs.plugins.googleGmsGoogleServices) apply false
}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(kotlin("gradle-plugin", version = "1.9.3"))
        classpath(libs.google.services.v442)


    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url=uri("$rootDir/../node_modules/react-native/android")
        }
        maven {
            // Android JSC is installed from npm
            url=uri("$rootDir/../node_modules/jsc-android/dist")
        }
        maven {
            // All of the Detox artifacts are provided via the npm module
            url= uri("$rootDir/../../../node_modules/detox/Detox-android")
        }


    }
}


