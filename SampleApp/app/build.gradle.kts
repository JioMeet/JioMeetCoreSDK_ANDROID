plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("com.google.dagger.hilt.android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.jio.sdksampleapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.jio.sdksampleapp"
        minSdk  = 21
        targetSdk  = 33
        versionCode = 2
        versionName = "2.1.8"


    }

    signingConfigs {
        register("release") {
            keyAlias = "com.jio.meet"
            keyPassword = "Jiomeet"
            storeFile = file("AndroidKeyStore.jks")
            storePassword = "Jiomeet"
        }

    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
            addAll(
                listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
                )
            )
        }
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    kotlin {
        jvmToolchain(11)
    }
    kotlinOptions {
        jvmTarget = ("11")
    }
}

repositories {
    maven {
        credentials {
            username = ""
            password = ""
        }
        url = uri("https://maven.pkg.github.com/JioMeet/JioMeetCoreSDK_ANDROID")
    }
    google()
    mavenCentral()
}


dependencies {
    implementation ("com.jiomeet.platform:jiomeetcoresdk:3.0.0")
    implementation ("androidx.core:core-ktx:1.9.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-android-compiler:2.44")
}
kapt {
    correctErrorTypes = true
}