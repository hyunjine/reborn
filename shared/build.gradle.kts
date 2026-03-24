@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.hyunjine.reborn.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }
}

kotlin {
    androidTarget()
    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.serialization)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.napier)
            implementation(libs.compose.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
