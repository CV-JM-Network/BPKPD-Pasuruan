import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.jaylangkung.bpkpduser"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jaylangkung.bpkpduser"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))
        buildConfigField("String", "API_KEY", localProperties["apiKey"] as String)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kotlin {
        jvmToolchain {
            this.languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    flavorDimensions += listOf("test")
    productFlavors {
        create("dev") {
            dimension = "test"
            versionNameSuffix = "dev"
            buildOutputs.all {
                val apkName = "User-BPKPD-Pasuruan" + "-" + defaultConfig.versionName + "-dev" + ".apk"
                val variantOutputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                variantOutputImpl.outputFileName = apkName
            }
        }
        create("production") {
            buildOutputs.all {
                val apkName = "User-BPKPD-Pasuruan" + "-" + defaultConfig.versionName + ".apk"
                val variantOutputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                variantOutputImpl.outputFileName = apkName
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.activity:activity:1.9.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.9")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.9")

    // RecyclerView & CardView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    // Toasty
    implementation("com.github.GrenderG:Toasty:1.5.0")

    // Material Dialog
    implementation("dev.shreyaspatil.MaterialDialog:MaterialDialog:2.2.3")

    // Loading Animation
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.github.chandreshandroid:MaterialProgressButton:1.7")

    // QR Code Scanner
    implementation("com.github.yuriy-budiyev:code-scanner:2.3.2")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0@aar")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.14.1")

    // Floating Action Button
    implementation("com.github.clans:fab:1.6.4")

    // Bottom Navigation
    implementation("nl.joery.animatedbottombar:library:1.1.0")
}
