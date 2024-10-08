import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.kuneosu.mintoners"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kuneosu.mintoners"
        minSdk = 24
        targetSdk = 34
        versionCode = 240819
        versionName = "1.0.7"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        val feedbackEmail = properties.getProperty("feedback_email")
        buildConfigField("String", "FEEDBACK_EMAIL", "\"$feedbackEmail\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    signingConfigs{
        create("release") {
            val properties = Properties()
            properties.load(project.rootProject.file("local.properties").inputStream())
            storeFile = file(properties.getProperty("store_file"))
            storePassword = properties.getProperty("store_password")
            keyPassword = properties.getProperty("key_password")
            keyAlias = properties.getProperty("key_alias")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            release {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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

    // Kotlin standard library
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Firebase
    implementation(libs.firebase.config.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // lottie
    implementation(libs.lottie)

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // SimpleToolTip
    implementation(libs.android.simple.tooltip)


}
