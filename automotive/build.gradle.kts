plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version "1.6.21"
    id("kotlin-parcelize")
    id("com.google.gms.google-services")

}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
kotlin{
    jvmToolchain{
        languageVersion.set(JavaLanguageVersion.of(17))
    }


}
android {
    compileSdkVersion(34)
    compileOptions{
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        applicationId = "com.example.laoapps"
        minSdkVersion(26)
        targetSdkVersion(33)
        versionCode = 4
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions{
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    namespace = "com.example.laoapps"
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.core.ktx.v170)
    implementation(libs.material3)
    implementation(libs.androidx.appcompat.v140)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose.v270)
    implementation(libs.activity.compose.v172)
    implementation(libs.lifecycle.runtime.ktx.v261)
    implementation(libs.androidx.material3.android)
    implementation(libs.annotations)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.volley)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.firestore)
    debugImplementation(libs.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview.v110)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)

    implementation(libs.androidx.core.ktx)
    implementation(libs.ui)
    implementation(libs.androidx.material3.v100alpha12)
    implementation("androidx.compose.material:material-icons-extended:")
    implementation("androidx.compose.ui:ui-tooling-preview:")
    implementation(libs.androidx.lifecycle.runtime.ktx.v241)
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.androidx.material.v131)
    debugImplementation("androidx.compose.ui:ui-tooling:")
    debugImplementation(libs.ui.test.manifest)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.prdownloader)
}