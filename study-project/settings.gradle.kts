rootProject.name = "study-project"

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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

//include(":m2l4-hw")
include(":study-project-api-v1-jackson")
include(":study-project-api-v1-mappers")
include(":study-project-api-v2-kmp")
include(":study-project-common")
include(":study-project-stubs")
