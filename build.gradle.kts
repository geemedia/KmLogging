
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.26.1")
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}
