plugins {
    kotlin("jvm")
}

repositories {
    maven {
        name = "LocalRepo"
        url = uri("${rootProject.projectDir}/../otus-kotlin-dcompose/build/repo")
    }
}

val resourcesFromLib by configurations.creating

dependencies {
    implementation(kotlin("stdlib"))

    resourcesFromLib("ru.demyanovaf.kotlin.taskManager:dcompose:1.0:resources@zip")

    implementation("ru.demyanovaf.kotlin.taskManager:study-project-api-v1-jackson")
    implementation("ru.demyanovaf.kotlin.taskManager:study-project-api-v1-mappers")
    implementation("ru.demyanovaf.kotlin.taskManager:study-project-api-v2-kmp")
    implementation("ru.demyanovaf.kotlin.taskManager:study-project-stubs")

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
    implementation(libs.kafka.client)
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn("extractLibResources")
    }
    register<Copy>("extractLibResources") {
        from(zipTree(resourcesFromLib.singleFile))
        into(layout.buildDirectory.dir("dcompose"))
    }
}
