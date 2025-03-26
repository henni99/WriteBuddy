import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.maven.publish)
}

mavenPublishing{


    coordinates("io.github.henni99", "writebuddy", "1.0.4")

    pom {
        name = "WriteBuddy"
        description = "https://github.com/henni99/writebuddy"
        url = "https://github.com/henni99/writebuddy"
        inceptionYear = "2025"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "henni99"
                name = "henni"
                email = "ansehoon1999@gmail.com"
            }
        }

        scm {
            url.set("https://github.com/henni99/writebuddy")
            connection.set("scm:git:git://github.com/henni99/WriteBuddy.git")
            developerConnection.set("scm:git:ssh://git@github.com/henni99/WriteBuddy.git")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()


}
kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "writebuddy-compose"
        browser {
            commonWebpackConfig {
                outputFileName = "writebuddy-compose.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.library()
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
        publishLibraryVariants("release","debug")
    }

    jvm("desktop")
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            configurations.commonMainApi {
                exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
            }
        }
    }
}

android {
    namespace = "com.henni.writebuddy"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {
    implementation(libs.androidx.material3.android)
}

dokka {
    dokkaSourceSets.commonMain {
        enableAndroidDocumentationLink = true
    }
    pluginsConfiguration.html {
        moduleVersion = "1.0.4"
    }
    dokkaPublications.html {
        outputDirectory = file("$rootDir/docs")
    }
}


