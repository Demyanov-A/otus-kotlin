plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.demyanovaf.kotlin.taskManager"
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

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-task-v1.yaml").toString())
    set("spec-v2", specDir.file("specs-task-v2.yaml").toString())
    set("spec-log1", specDir.file("specs-task-log1.yaml").toString())
}

tasks {
    fun taskRegistration(taskName: String, taskGroup: String){
        register(taskName) {
            group = taskGroup
            subprojects.forEach { proj ->
                println("PROJ $proj")
                proj.getTasksByName(taskName, false).also {
                    this@register.dependsOn(it)
                }
            }
        }
    }

    taskRegistration("build","build")
    taskRegistration("clean","build")
    taskRegistration("check","verification")
}