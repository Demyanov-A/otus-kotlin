plugins {
    id("build-docker") apply false
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
        dependsOn(project(":ok-dcompose").getTasksByName("publish",false))
        dependsOn(project(":ok-migration-pg").getTasksByName("buildImages",false))
    }

    register("clean"){group = "build"}
}