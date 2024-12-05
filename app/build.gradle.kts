plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.capstone.aiskin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.capstone.aiskin"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.ui.desktop)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Glide for showing online image
    implementation(libs.glide)

    //noinspection GradleDependency
    implementation (libs.androidx.activity.ktx)
    //noinspection GradleDependency,GradleDependency
    implementation (libs.androidx.fragment.ktx)


    //Library TensorFlow Lite
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support.v044)
    implementation(libs.tensorflow.lite.metadata.v044)
    implementation(libs.tensorflow.lite.task.vision)

    // Api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Sekeleton
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

}