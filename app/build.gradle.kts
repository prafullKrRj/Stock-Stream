import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.prafullkumar.stockstream"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.prafullkumar.stockstream"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_KEY_1", "\"${getApiKey("API_KEY_1")}\"")
        buildConfigField("String", "API_KEY_2", "\"${getApiKey("API_KEY_2")}\"")
        buildConfigField("String", "API_KEY_3", "\"${getApiKey("API_KEY_3")}\"")
        buildConfigField("String", "API_KEY_4", "\"${getApiKey("API_KEY_4")}\"")
        buildConfigField("String", "API_KEY_5", "\"${getApiKey("API_KEY_5")}\"")
        buildConfigField("String", "API_KEY_6", "\"${getApiKey("API_KEY_6")}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
        compose = true
        buildConfig = true
    }
}
fun getApiKey(
    key: String,
): String {
    val localPropertiesFile = rootProject.file("local.properties")
    val localProperties = Properties()

    println("Looking for local.properties at: ${localPropertiesFile.absolutePath}")
    println("File exists: ${localPropertiesFile.exists()}")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
        val apiKey = localProperties.getProperty(key, "default_key")
        println("Loaded API_KEY: $apiKey")
        return apiKey
    }

    println("local.properties not found, using default")
    return "default_key"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // paging library
    implementation(libs.androidx.paging.compose)
    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // kotlin json serialization
    implementation(libs.kotlinx.serialization.json)

    // icons
    implementation(libs.androidx.material.icons.extended)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)

    // viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // jetpack navigation
    implementation(libs.androidx.navigation.compose)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.converter.gson)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.compose.charts)
    implementation(libs.androidx.core.splashscreen)
}