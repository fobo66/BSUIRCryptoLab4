rootProject.name = "lab4"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.8.10")
            version("rng", "1.6")
            library("cli", "org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
            library("rng.core", "org.apache.commons", "commons-rng-core").versionRef("rng")
            library("rng.simple", "org.apache.commons", "commons-rng-simple").versionRef("rng")
            library("rng.client", "org.apache.commons", "commons-rng-client-api").versionRef("rng")
        }
    }
}
