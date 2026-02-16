plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Внутренние модели
    implementation(project(":study-project-common"))
    implementation(project(":study-project-app-common"))
    implementation("ru.demyanovaf.kotlin.taskManager.libs:study-project-lib-logging-logback")

    // v1 api
    implementation(project(":study-project-api-v1-jackson"))
    implementation(project(":study-project-api-v1-mappers"))

    // v2 api
    implementation(project(":study-project-api-v2-kmp"))

    // biz
    implementation(project(":study-project-biz"))

    // DB
    implementation(project(":study-project-repo-stubs"))
    implementation(project(":study-project-repo-inmemory"))
    implementation(project(":study-project-repo-pgjvm"))
    testImplementation(project(":study-project-repo-common"))
    testImplementation(project(":study-project-stubs"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.spring.mockk)
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1", "spec-v2").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    environment("MGRTASK_DB", "test_db")
}

tasks.bootBuildImage {
    builder = "paketobuildpacks/builder-jammy-base:0.4.539"
    environment.set(mapOf("BP_HEALTH_CHECKER_ENABLED" to "true"))
    buildpacks.set(
        listOf(
            "docker.io/paketobuildpacks/adoptium",
            "urn:cnb:builder:paketo-buildpacks/java",
            "docker.io/paketobuildpacks/health-checker:latest"
        )
    )

}
