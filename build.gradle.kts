plugins {
    application
    kotlin("jvm") version "1.7.10"
}

application {
    mainClass.set("io.fobo66.crypto.Lab4")
}

allprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.apache.commons:commons-rng-core:1.4")
        implementation("org.apache.commons:commons-rng-simple:1.4")
        implementation("org.apache.commons:commons-rng-client-api:1.4")
        implementation("org.jetbrains:annotations:23.0.0")
        implementation("commons-cli:commons-cli:1.5.0")
    }
}
