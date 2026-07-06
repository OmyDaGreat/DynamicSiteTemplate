@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
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
    pagesPackage = "xyz.malefic.dynamicsite.client.pages"
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

val jvmJar = tasks.named<Jar>("jvmJar")
val kobwebStaticSite =
    tasks.register<Copy>("kobwebStaticSite") {
        description = "Copies the generated Kobweb production site into the server-visible build directory."

        dependsOn("jsBrowserDistribution")

        into(layout.projectDirectory.dir(".kobweb/site"))

        from(layout.buildDirectory.dir("dist/js/productionExecutable"))
    }

val dockerRuntime =
    tasks.register<Copy>("dockerRuntime") {
        description = "Prepares the application for Docker by copying the necessary files into a build directory."

        dependsOn(jvmJar)
        dependsOn(kobwebStaticSite)

        into(layout.buildDirectory.dir("docker"))

        from(jvmJar) {
            rename { "app.jar" }
        }

        from(configurations.getByName("jvmRuntimeClasspath")) {
            into("lib")
        }

        from(layout.projectDirectory.dir(".kobweb/site")) {
            into("site/.kobweb/site")
        }
    }

tasks.named("build") {
    dependsOn(dockerRuntime)
}

afterEvaluate {
    afterEvaluate {
        tasks.named("jvmRun") {
            dependsOn(kobwebStaticSite)
        }
    }
}

configurations.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-test")) {
            useVersion(libs.versions.kotlin.get())
        }
    }
}
