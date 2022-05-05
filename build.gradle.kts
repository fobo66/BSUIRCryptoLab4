plugins {
    application
    kotlin("jvm") version "1.3.0"
}

application {
    mainClassName = "io.fobo66.crypto.Lab4"
}

allprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.apache.commons:commons-rng-core:1.1")
        implementation("org.apache.commons:commons-rng-simple:1.1")
        implementation("org.apache.commons:commons-rng-client-api:1.1")
        implementation("org.jetbrains:annotations:16.0.3")
        implementation("commons-cli:commons-cli:1.5.0")
        implementation(kotlin("stdlib-jdk8"))
    }
}