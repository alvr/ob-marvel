plugins {
    id("modules.android-library")
}

android {
    buildFeatures.buildConfig = true

    defaultConfig {
        buildConfigField("String", "MARVEL_PUBLIC_KEY", "\"8cbd891cfe18e0973bc6c225980e5be2\"")
        buildConfigField("String", "MARVEL_PRIVATE_KEY", "\"b45714df373750d09324f9f1dc2b5f9130cf3c75\"")
    }
}

dependencies {
    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)
    implementation(libs.retrofit.serialization)

    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
