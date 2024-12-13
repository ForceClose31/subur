import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") version "4.4.2"
    id("kotlin-kapt")
}

android {
    namespace = "com.bangkit.subur"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bangkit.subur"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    defaultConfig {

        val p = Properties()
        p.load(rootProject.file("local.properties").reader())
        val apiKeyGemini: String = p.getProperty("API_KEY_GEMINI")
        val apiKeyWeather: String = p.getProperty("API_KEY_WEATHER")

        buildConfigField("String", "API_KEY_GEMINI", apiKeyGemini)
        buildConfigField("String", "API_KEY_WEATHER", apiKeyWeather)

    }

    buildTypes {

        release {
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
}

dependencies {
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("androidx.datastore:datastore-preferences:1.1.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation (libs.generativeai)

    //  retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.gson)

    //  koin
    implementation(libs.insert.koin.koin.android)

    // camera
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle.v120)
    implementation (libs.androidx.camera.view.v100)

    implementation (libs.glide)
    kapt (libs.androidx.room.compiler)

    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.cardview)
    implementation (libs.picasso)

    implementation("com.google.firebase:firebase-auth")

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.2")


}