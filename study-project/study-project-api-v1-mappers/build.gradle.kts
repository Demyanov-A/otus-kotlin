plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.studyProjectApiV1Jackson)
    implementation(projects.studyProjectCommon)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.studyProjectStubs)
}
