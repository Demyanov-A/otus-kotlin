plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.demyanovaf.kotlin.taskManager"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

tasks {
    register("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }
    val buildMigrations by registering { ->
        dependsOn(
            gradle.includedBuild("otus-kotlin-dcompose").task(":buildInfra")
        )
    }

    val buildImages by registering { ->
        dependsOn(buildMigrations)
        dependsOn(gradle.includedBuild("study-project").task(":buildImages"))
    }

    val e2eTests by registering { ->
        dependsOn(buildImages)
        dependsOn(gradle.includedBuild("study-project-tests").task(":e2eTests"))
        mustRunAfter(buildImages)
    }

    register("check") {
        group = "verification"
        dependsOn(gradle.includedBuild("study-project").task(":check"))
        dependsOn(e2eTests)
    }
}

