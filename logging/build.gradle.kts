import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("com.jfrog.artifactory")
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    js(BOTH) {
        browser {
        }
    }
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("co.touchlab:stately-concurrency:1.1.10")
            }
        }

        val androidMain by getting

        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.30")
            }
        }

        val iosArm64Main by getting
        val iosX64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosX64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val jsMain by getting
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 14
        consumerProguardFiles("proguard.txt")
    }
}

tasks {
    create<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        dependsOn(dokkaHtml)
        from(dokkaHtml.get().outputDirectory)
    }
}

extra["artifactId"] = "kmlogging"
extra["artifactVersion"] = "1.1.2-anuvu"
extra["libraryName"] = "KmLogging: Kotlin Multiplatform Logging"
extra["libraryDescription"] = "KmLogging is a high performance, extensible and easy to use logging library for Kotlin Multiplatform development"
extra["gitUrl"] = "https://github.com/LighthouseGames/KmLogging"

apply(from = "publish.gradle.kts")

// defined in user's global gradle.properties
val artifactory_publish_url: String? by project
val artifactory_publish_repo: String? by project
val artifactory_publish_username: String? by project
val artifactory_publish_password: String? by project

if (project.hasProperty("artifactory_publish_url")) {
    configure<org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention> {
        setContextUrl(artifactory_publish_url)
        publish {
            repository {
                setRepoKey(artifactory_publish_repo)
                setUsername(artifactory_publish_username)
                setPassword(artifactory_publish_password)
            }
            defaults {
                publications("androidDebug", "androidRelease",
                    "ios", "iosArm64", "iosX64", "iosSimulatorArm64",
                    "kotlinMultiplatform", "metadata"
                )
                setPublishArtifacts(true)
                setProperties(mapOf("qa.level" to "basic", "dev.team" to "core"))
                setPublishPom(true)
            }
        }
    }
}
