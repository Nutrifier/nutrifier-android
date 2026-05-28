import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
}

android {
    namespace = "fi.nutrifier"
    compileSdk = 36

    // Load keystore
    val keystoreFile = rootProject.file("api.keystore")
    val properties = Properties().apply {
        load(keystoreFile.inputStream())
    }

    val apiKey = properties.getProperty("API_KEY") ?: ""
    val releaseBaseUrl = properties.getProperty("RELEASE_BASE_URL") ?: ""
    val appUUID = properties.getProperty("APP_UUID") ?: ""
    val fineliUUID = properties.getProperty("FINELI_UUID") ?: ""

    defaultConfig {
        applicationId = "fi.nutrifier"
        minSdk = 26
        targetSdk = 34
        versionCode = 13
        versionName = "1.1.7-beta"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_KEY", apiKey)
        buildConfigField("String", "APP_UUID", appUUID)
        buildConfigField("String", "FINELI_UUID", fineliUUID)
    }

    buildTypes {
        debug {
            // Use 10.0... url for emulators and 127.0... for actual devices
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
            //buildConfigField("String", "BASE_URL", "\"http://127.0.0.1:8080/\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", releaseBaseUrl)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        animationsDisabled = true
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Added dependencies
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.coil.compose.v260)
    implementation(libs.androidx.material.icons.extended.v167)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Needed for coroutine support
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.security.crypto)
    implementation (libs.logging.interceptor)
    ksp(libs.androidx.room.compiler)

    // Barcode dependencies
    implementation(libs.barcode.scanning)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.kotlinx.serialization.json)
}