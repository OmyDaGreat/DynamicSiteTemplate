@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
}

group = "xyz.malefic.dynamicsite"
version = "1.0.0"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
        }
    }
}

kotlin {
    configAsKobwebApplication("dynamicsite")

    jvm {
        mainRun {
            mainClass = "xyz.malefic.dynamicsite.server.MainKt"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }

        jsMain.dependencies {
            implementation(libs.bundles.compose)
            implementation(libs.bundles.kobweb)
            implementation(libs.bundles.silk.icons)
            implementation(libs.kutint)
        }

        jvmMain.dependencies {
            implementation(libs.bundles.http4k)
            implementation(libs.http4k.format.kotlinx)
            compileOnly(libs.kobweb.api)
        }
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JVM_21)
    }
}
