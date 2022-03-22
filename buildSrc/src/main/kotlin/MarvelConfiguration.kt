@file:Suppress("InvalidPackageDeclaration")

import org.gradle.api.JavaVersion

object MarvelConfiguration {
    const val CompileSdk = 31
    const val BuildTools = "31.0.0"
    const val PackageName = "dev.alvr.marvel"
    const val MinSdk = 21
    const val TargetSdk = 31
    const val VersionName = "0.0.1"
    const val VersionCode = 1

    val UseJavaVersion = JavaVersion.VERSION_11
    const val JvmTarget = "11"
    const val KotlinVersion = "1.6"
}
