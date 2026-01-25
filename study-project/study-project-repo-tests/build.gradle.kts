plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                api(libs.coroutines.core)
                api(libs.coroutines.test)
                implementation(projects.studyProjectCommon)
                implementation(projects.studyProjectRepoCommon)
            }
        }
        commonTest {
            dependencies {
                implementation(projects.studyProjectStubs)
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}
