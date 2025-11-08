plugins {
    id("build-jvm")
    id("maven-publish")
}

group = "ru.demyanovaf.kotlin.taskManager.tests"
version = "0.1.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("buildInfra") {
        group = "build"
        dependsOn(project(":otus-kotlin-dcompose").getTasksByName("publish",false))
    }
}