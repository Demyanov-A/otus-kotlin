plugins {
    id("build-jvm")
    application
}

application {
    mainClass.set("ru.demyanovaf.kotlin.taskManager.app.tmp.MainKt")
}

dependencies {
    implementation(project(":study-project-api-log1"))
    implementation("ru.demyanovaf.kotlin.taskManager.libs:study-project-lib-logging-common")
    implementation("ru.demyanovaf.kotlin.taskManager.libs:study-project-lib-logging-logback")

    implementation(project(":study-project-common"))

    implementation(libs.coroutines.core)
}
