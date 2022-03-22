package utils

import MarvelConfiguration
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun BaseExtension.baseAndroidConfig() {
    compileSdkVersion(MarvelConfiguration.CompileSdk)
    buildToolsVersion(MarvelConfiguration.BuildTools)

    defaultConfig {
        minSdk = MarvelConfiguration.MinSdk
        targetSdk = MarvelConfiguration.TargetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFile("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = MarvelConfiguration.UseJavaVersion
        targetCompatibility = MarvelConfiguration.UseJavaVersion
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                test.useJUnitPlatform()
            }
        }
    }
}

fun KotlinJvmOptions.configureKotlin() {
    jvmTarget = MarvelConfiguration.JvmTarget
    apiVersion = MarvelConfiguration.KotlinVersion
    languageVersion = MarvelConfiguration.KotlinVersion
    freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}
