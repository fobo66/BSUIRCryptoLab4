import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.7.20"
}

application {
    mainClass.set("dev.fobo66.crypto.Lab4Kt")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation("org.apache.commons:commons-rng-core:1.4")
    implementation("org.apache.commons:commons-rng-simple:1.4")
    implementation("org.apache.commons:commons-rng-client-api:1.4")
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("commons-cli:commons-cli:1.5.0")
}
