# Setup Hilt in your project

**Note**: Hilt is required for version v2.3.1 or before

To set up Hilt in your Android project, follow these steps:

1. First, add the hilt-android-gradle-plugin plugin to your project’s root build.gradle file:

   ```gradle
   plugins {
   id("com.google.dagger.hilt.android") version "2.44" apply false
   }
   ```

2. Add the Hilt dependencies to the app-level build.gradle file

   ````gradle
   plugins {
     kotlin("kapt")
     id("com.google.dagger.hilt.android")
   }

   android {
       ...
       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_11
           targetCompatibility = JavaVersion.VERSION_11
       }
   }

   dependencies {
           implementation "androidx.hilt:hilt-navigation-compose:1.0.0"
           implementation "com.google.dagger:hilt-android:2.44"
           kapt "com.google.dagger:hilt-android-compiler:2.44"
   } ```
   ````
