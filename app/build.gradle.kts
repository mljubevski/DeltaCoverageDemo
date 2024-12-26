import io.github.surpsg.deltacoverage.CoverageEngine
import org.gradle.kotlin.dsl.resolver.buildSrcSourceRootsFilePath

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kover)
    alias(libs.plugins.delta.coverage)
}

android {
    namespace = "com.example.deltacoveragedemo"
    compileSdk = 35

    sourceSets {
        named("main") {
            java.srcDirs("src/java")
            resources.srcDir("src/resources")
        }
    }

    defaultConfig {
        applicationId = "com.example.deltacoveragedemo"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    }
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
}

kover {
    
}

configure<io.github.surpsg.deltacoverage.gradle.DeltaCoverageConfiguration> {
    coverage {
        engine = CoverageEngine.INTELLIJ
    }
    
    classesDirs = files("${layout.buildDirectory}/tmp/kotlin-classes/debug")


    diffSource {
        git.compareWith("refs/remotes/origin/master")
    }
    
    reportViews {
        view("view") {
            coverageBinaryFiles = files(
                "$projectDir/app/build/kover/bin-reports/testDebugUnitTest.ic",
                "$projectDir/app/build/kover/bin-reports/testReleaseUnitTest.ic"
            )

            matchClasses.value(
                listOf("app/src/main/java/**/*.kt")
            )
        }
    }
}