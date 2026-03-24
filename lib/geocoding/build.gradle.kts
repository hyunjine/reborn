plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.hyunjine.reborn"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(libs.jts.core)
    implementation(libs.kotlin.serialization)

    testImplementation(libs.kotlin.test)
}
