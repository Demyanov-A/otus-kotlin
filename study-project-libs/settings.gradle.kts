rootProject.name = "study-project-libs"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

include(":study-project-lib-logging-common")
include(":study-project-lib-logging-kermit")
include(":study-project-lib-logging-logback")
include(":study-project-lib-logging-socket")
include(":study-project-lib-cor")
