/*
 *  Copyright 2025 CNM Ingenuity, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.io.FileInputStream
import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.schema.parser)
    alias(libs.plugins.junit)
}

android {

    namespace = project.property("basePackageName") as String?
    compileSdk = (project.property("targetSdk") as String).toInt()

    defaultConfig {

        applicationId = project.property("basePackageName") as String
        minSdk = (project.property("minSdk") as String).toInt()
        targetSdk = (project.property("targetSdk") as String).toInt()
        versionCode = (project.property("versionCode") as String).toInt()
        versionName = project.property("version") as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"

        resValue("string", "app_name", project.property("appName") as String)
//        resValue("string", "client_id", getLocalProperty("clientId") as String)

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
        targetCompatibility = JavaVersion.valueOf("VERSION_${libs.versions.java.get()}")
    }

    buildFeatures {
        viewBinding = true
        // Enable dataBinding if desired.
        // dataBinding = true
    }

}

dependencies {

    // .jar-based libraries included in project

    // Basic Android components
    implementation(libs.app.compat)
    implementation(libs.activity)
    implementation(libs.fragment)
    implementation(libs.constraint.layout)
    implementation(libs.recycler.view)

    // Navigation framework libraries
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Lifecycle (LiveData and ViewModel) libraries
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Preferences/settings components
    implementation(libs.preference)

    // Material Design components
    implementation(libs.material)

    // ReactiveX libraries
    implementation(libs.rx.java)
    implementation(libs.rx.android)

    // Room annotation processor, runtime library, and ReactiveX integration
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.rx.java)

    // Gson (Google JSON parser) library
    implementation(libs.gson)

    // Google Sign-in library
    implementation(libs.play.auth)

    // Retrofit (REST client) with ReactiveX & Gson integration
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rx.java)

    // Hilt dependency-injection library & annotation processor
    implementation(libs.hilt.android.core)
    annotationProcessor(libs.hilt.compiler)

    // OkHttp logging dependency
    implementation(libs.logging.interceptor)

    // Libraries for JVM-based testing.
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)

    // Libraries for instrumented (run in Android) testing.
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.junit.android.core)
    androidTestRuntimeOnly(libs.junit.android.runner)
    androidTestImplementation(libs.junit.api)
    androidTestImplementation(libs.junit.params)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestAnnotationProcessor(libs.hilt.compiler)
    androidTestAnnotationProcessor(libs.hilt.android.compiler)

    constraints {
        implementation(libs.kotlin.jdk7) {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        //noinspection ForeignDelegate
        implementation(libs.kotlin.jdk8) {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
}

roomDdl {
    source.set(project.file("$projectDir/schemas/edu.cnm.deepdive.capstone.service.LocalDatabase/1.json"))
    destination.set(project.file("$projectDir/../docs/sql/ddl.sql"))
}

val documentationUmbrella = tasks.register("javadoc") {
    group = "documentation"
    description = "Generates Javadoc HTML for all build variants. (Generated output for variants " +
            "after the first will overwrite the output for previous variants.)"
}

android.applicationVariants.configureEach {

    val simpleName = name
    val variantName = name.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale.getDefault())
        } else {
            it.toString()
        }
    }

    val docTitle = "${project.property("appName")} ${android.defaultConfig.versionName}"

    val task = project.tasks.register("javadoc${variantName}", Javadoc::class.java) {

        dependsOn(tasks.named("assemble$variantName"))
        title = docTitle
        group = "documentation"
        description = "Generates Javadoc for $simpleName build variant."

        source = javaCompileProvider.get().source
        exclude(
            "**/*FragmentDirections.java",
            "**/*FragmentArgs.java",
            "**/databinding/*.java",
            "**/*_*.java",
            "**/R.java",
            "**/BuildConfig*.java"
        )

        if (project.hasProperty("javadocDestDir")) {
            setDestinationDir(
                projectDir.toPath().resolve(project.property("javadocDestDir") as String).toFile()
            )
        }

        doFirst {
            classpath = project.files(
                projectDir.resolve("build/intermediates/javac/$simpleName/compile${variantName}JavaWithJavac/classes"),
                javaCompileProvider.get().classpath.files,
                android.bootClasspath
            )
        }

        with(options as StandardJavadocDocletOptions) {
            windowTitle = docTitle
            memberLevel = JavadocMemberLevel.PROTECTED
            isLinkSource = true
            isAuthor = false
            links(
                "https://docs.oracle.com/en/java/javase/${libs.versions.java.get()}/docs/api/",
                "https://javadoc.io/doc/com.google.dagger/dagger/${libs.versions.hilt.get()}/",
                "https://javadoc.io/doc/com.google.dagger/hilt-android/latest/",
                "https://reactivex.io/RxJava/3.x/javadoc/"
            )
            linksOffline("https://developer.android.com/reference", "$projectDir")
            addBooleanOption("html5", true)
            addStringOption("Xdoclint:none", "-quiet")
        }

        isFailOnError = true
    }

    documentationUmbrella.dependsOn(task)
}

fun getLocalProperty(name: String): String {
    return getProperty("$projectDir/local.properties", name)
}

fun getProperty(filename: String, name: String): String {
    return FileInputStream(filename).use {
        val props = Properties()
        props.load(it)
        props.getProperty(name)
    }
}
