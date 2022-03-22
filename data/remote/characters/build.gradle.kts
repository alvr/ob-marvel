plugins {
    id("modules.android-library")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(projects.data.remote.base)
    implementation(projects.domain.characters)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
