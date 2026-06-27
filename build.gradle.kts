import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    application
    alias(libs.plugins.kotlin)
    alias(libs.plugins.detekt)
}

application {
    mainClass = "dev.fobo66.crypto.Lab4Kt"
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useKotlinTest(libs.versions.kotlin)
        }
    }
}

tasks {
    withType<dev.detekt.gradle.Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "25"
    }
    withType<dev.detekt.gradle.DetektCreateBaselineTask>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "25"
    }

    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.rng.core)
    implementation(libs.rng.simple)
    implementation(libs.rng.client)
    implementation(libs.clikt)
}
