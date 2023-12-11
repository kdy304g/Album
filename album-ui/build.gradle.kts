plugins {
    kotlin("kapt")
    id("com.android.library")
    id("kotlin-android")
    id("com.google.dagger.hilt.android") version "2.47" apply false
}

android {
    namespace = "com.example.album_ui"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    implementation(project(":album-domain"))
    implementation(project(":album-core"))
    implementation(project(":album-player"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.compose.material:material:1.5.4")

    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha")

    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation ("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")
    implementation("androidx.media3:media3-session:1.2.0")

}