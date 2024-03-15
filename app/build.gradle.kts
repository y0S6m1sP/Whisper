plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleDaggerHiltAndroid)
    alias(libs.plugins.googleGmsGoogleService)
    alias(libs.plugins.jetbrainsKotlinPluginSerialization)
    alias(libs.plugins.googleDevtoolsKsp)
}

android {
    namespace = "com.rocky.whisper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rocky.whisper"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.rocky.shared_test.CustomTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/*"
        }
    }
}

dependencies {

    val composeBom = platform(libs.androidx.compose.bom)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(composeBom)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android.core)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.kotlin.serialization)
    implementation(libs.timber)
    implementation(libs.coil.compose)

    testImplementation(composeBom)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.test)
    testImplementation(libs.kotlin.coroutine.android)
    testImplementation(libs.kotlin.coroutine.test)
    testImplementation(libs.androidx.navigation.test)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.espresso.contrib)
    testImplementation(libs.androidx.espresso.intents)
    testImplementation(libs.google.truth)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.hilt.android.test)
    kspTest(libs.hilt.compiler)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.androidx.test.junit.ktx)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.mockk)
    testImplementation(project(":shared-test"))

    androidTestImplementation(composeBom)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.kotlin.coroutine.test)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.junit.ktx)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.room.test)
    androidTestImplementation(libs.androidx.core.test)
    androidTestImplementation(libs.androidx.navigation.test)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.espresso.idling.resource)
    androidTestImplementation(libs.androidx.espresso.idling.concurrent)
    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(project(":shared-test"))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}