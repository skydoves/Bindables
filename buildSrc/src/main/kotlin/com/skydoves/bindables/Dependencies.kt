package com.skydoves.bindables

object Versions {
  internal const val ANDROID_GRADLE_PLUGIN = "8.7.1"
  internal const val ANDROID_GRADLE_SPOTLESS = "6.3.0"
  internal const val GRADLE_NEXUS_PUBLISH_PLUGIN = "1.1.0"
  internal const val KOTLIN = "1.7.0"
  internal const val KOTLIN_GRADLE_DOKKA = "1.6.21"
  internal const val KOTLIN_BINARY_VALIDATOR = "0.10.1"

  internal const val APPCOMPAT = "1.4.1"
  internal const val MATERIAL = "1.6.0"
  internal const val LIFECYCLE = "2.4.1"

  internal const val FRAGMENT = "1.2.5"
  internal const val HILT = "2.42"
  internal const val GLIDE = "4.13.0"
  internal const val WHATIF = "1.1.1"
}

object Dependencies {
  const val androidGradlePlugin =
    "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
  const val gradleNexusPublishPlugin =
    "io.github.gradle-nexus:publish-plugin:${Versions.GRADLE_NEXUS_PUBLISH_PLUGIN}"
  const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
  const val spotlessGradlePlugin =
    "com.diffplug.spotless:spotless-plugin-gradle:${Versions.ANDROID_GRADLE_SPOTLESS}"
  const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.KOTLIN_GRADLE_DOKKA}"
  const val kotlinBinaryValidator =
    "org.jetbrains.kotlinx:binary-compatibility-validator:${Versions.KOTLIN_BINARY_VALIDATOR}"

  const val appcompat = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
  const val material = "com.google.android.material:material:${Versions.MATERIAL}"
  const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:${Versions.LIFECYCLE}"
  const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
  const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
  const val viewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.LIFECYCLE}"

  const val fragment = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
  const val hilt = "com.google.dagger:hilt-android:${Versions.HILT}"
  const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.HILT}"
  const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
  const val glide = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
  const val whatIf = "com.github.skydoves:whatif:${Versions.WHATIF}"
}
