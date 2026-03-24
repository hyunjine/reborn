plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.hyunjine.reborn"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations.all {
    exclude(group = "org.jetbrains.compose.runtime", module = "runtime-desktop")
}

extra["kotlin-serialization.version"] = libs.versions.kotlinSerialization.get()

dependencies {
    implementation(projects.shared)
    implementation(projects.lib.geocoding)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.exposed.spring.boot.starter)
    implementation(libs.exposed.kotlin.datetime)
    runtimeOnly(libs.postgresql)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.kotlin.test)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Exec>("deployDocker") {
    group = "deployment"
    description = "Build bootJar and deploy with Docker Compose"
    dependsOn("bootJar")
    workingDir = projectDir
    commandLine("docker", "compose", "up", "-d", "--build")
}
