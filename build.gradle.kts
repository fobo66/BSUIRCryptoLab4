plugins {
    application
    kotlin("jvm") version libs.versions.kotlin
}

application {
    mainClass.set("dev.fobo66.crypto.Lab4Kt")
}

kotlin {
    jvmToolchain(17)
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
