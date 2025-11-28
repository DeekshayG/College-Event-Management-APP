// File: C:/Users/Deekshay/AndroidStudioProjects/CollegeEventApp/build.gradle.kts

// Top-level build.gradle.kts

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        // Android Gradle Plugin
        classpath("com.android.tools.build:gradle:8.3.1")
        // Google Services Plugin (for Firebase)
        classpath("com.google.gms:google-services:4.4.0")
    }
}

plugins {
    // These define available plugins but don't apply them yet
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
}

//allprojects {
 //   repositories {
 //       google()
 //       mavenCentral()
//    }
//}


// NO extra closing brace here
