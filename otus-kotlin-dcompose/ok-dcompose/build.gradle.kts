plugins {
    id("build-jvm")
    id("maven-publish")
}

group = "ru.demyanovaf.kotlin.taskManager.tests"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

val resourcesZip = tasks.register<Zip>("resourcesZip") {
    archiveClassifier.set("resources")
    from("dcompose")
}

// Публикация
publishing {
    repositories {
        maven {
            name = "LocalRepo"
            url = uri("${rootProject.projectDir}/build/repo")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "ru.demyanovaf.kotlin.taskManager"
            artifactId = "dcompose"
            version = "1.0"

            from(components["java"])

            artifact(resourcesZip) {
                classifier = "resources"
                extension = "zip"
            }
        }
    }
}
