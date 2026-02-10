plugins {
    id("build-jvm")
}
repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.studyProjectCommon)
    api(projects.studyProjectRepoCommon)

    implementation(libs.coroutines.core)
    implementation(libs.uuid)

    implementation(libs.db.postgres)
//  implementation(libs.db.hikari)
    implementation(libs.bundles.exposed)

    testImplementation(kotlin("test-junit"))
    testImplementation(projects.studyProjectRepoTests)
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.logback)

}
