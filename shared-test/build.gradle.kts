plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.rocky.shared_test"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(project(":app"))
    implementation(composeBom)
    implementation(libs.androidx.ui)
    implementation(libs.kotlin.coroutine.android)
    implementation(libs.kotlin.coroutine.test)
    implementation(libs.junit)
    implementation(libs.androidx.test.core.ktx)
    implementation(libs.androidx.test.junit.ktx)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.core)
    implementation(libs.hilt.android.test)
    ksp(libs.hilt.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
}