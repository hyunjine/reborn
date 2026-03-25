rootProject.name = "Reborn"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Gradle sync 시 ADB 무선 연결 자동 수행
try {
    ProcessBuilder("adb", "connect", "192.168.1.97:5555")
        .redirectErrorStream(true)
        .start()
} catch (_: Exception) {
}

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":shared")
include(":composeApp")
include(":server")
include(":lib:geocoding")