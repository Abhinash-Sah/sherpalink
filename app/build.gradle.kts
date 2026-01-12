plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.sherpalink"

    // ✅ Fixes AAR Metadata errors by supporting latest AndroidX libraries
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sherpalink"
        minSdk = 24
        targetSdk = 35 // Targets Android 15 behaviors
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // ✅ NDK r27+ is required to properly align .so files to 16 KB boundaries
    ndkVersion = "27.0.12077973"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        jniLibs {
            // ✅ CRITICAL for 16 KB support:
            // Setting to false ensures libraries are stored uncompressed in the APK.
            useLegacyPackaging = false
        }
    }
}

// ✅ Fixes the 16 KB alignment error by forcing Cloudinary's
// internal dependencies (Fresco) to version 3.2.0+
configurations.all {
    resolutionStrategy {
        force("com.facebook.fresco:fresco:3.2.0")
        force("com.facebook.fresco:imagepipeline-okhttp3:3.2.0")
        force("com.facebook.fresco:ui-common:3.2.0")
        force("com.facebook.fresco:imagepipeline:3.2.0")
        force("com.facebook.fresco:fbcore:3.2.0")
        force("com.facebook.fresco:middleware:3.2.0")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.material:material-icons-extended")

    // ✅ Using Coil 3.0.4 for full Android 15/16 compatibility
    // NOTE: Update your imports in AdminUpload.kt to 'import coil3.compose.AsyncImage'
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    implementation("com.cloudinary:cloudinary-android:2.1.0")

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation("com.google.maps.android:maps-compose:4.3.3")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.runtime.livedata)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}