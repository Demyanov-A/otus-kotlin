plugins {
    id("build-jvm")
    application
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.demyanovaf.kotlin.taskManager.app.rabbit.ApplicationKt")
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(libs.rabbitmq.client)
    implementation(libs.jackson.databind)
    implementation(libs.logback)
    implementation(libs.coroutines.core)

    implementation(project(":study-project-common"))
    implementation(project(":study-project-app-common"))
    implementation("ru.demyanovaf.kotlin.taskManager.libs:study-project-lib-logging-logback")

    // v1 api
    implementation(project(":study-project-api-v1-jackson"))
    implementation(project(":study-project-api-v1-mappers"))

    // v2 api
    implementation(project(":study-project-api-v2-kmp"))

    implementation(project(":study-project-biz"))
    implementation(project(":study-project-stubs"))

    testImplementation(libs.testcontainers.rabbitmq)
    testImplementation(kotlin("test"))
}
