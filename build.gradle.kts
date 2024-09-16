import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    application
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.detekt)
}

application {
    mainClass = "dev.fobo66.crypto.Lab4Kt"
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest(libs.versions.kotlin)
        }
    }
}

dependencies {
    implementation(libs.rng.core)
    implementation(libs.rng.simple)
    implementation(libs.rng.client)
    implementation(libs.cli)
}
