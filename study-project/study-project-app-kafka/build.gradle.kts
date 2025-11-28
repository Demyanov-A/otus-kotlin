plugins {
    application
    id("build-jvm")
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.demyanovaf.kotlin.taskManager.app.kafka.MainKt")
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("ru.demyanovaf.kotlin.taskManager.libs:study-project-lib-logging-logback")

    implementation(project(":study-project-app-common"))

    // transport models
    implementation(project(":study-project-common"))
    implementation(project(":study-project-api-v1-jackson"))
    implementation(project(":study-project-api-v1-mappers"))
    implementation(project(":study-project-api-v2-kmp"))
    // logic
    implementation(project(":study-project-biz"))

    testImplementation(kotlin("test-junit"))
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }
}

