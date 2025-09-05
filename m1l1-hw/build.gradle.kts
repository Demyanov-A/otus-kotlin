plugins {
    kotlin("jvm")
}

group = "ru.demyanovaf.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin{
    jvmToolchain(21)
}