plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
        alias(libs.plugins.kotlin.android)

}

android {
    namespace = "com.example.farmingmanagemengsystempartial"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.farmingmanagemengsystempartial"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0") // Ensure this is correct
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.android.material:material:1.7.0") //DatePicker Material
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

}