///*
// * Designed and developed by 2021 skydoves (Jaewoong Eum)
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */

import com.skydoves.bindables.Configuration

plugins {
  id(libs.plugins.android.application.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
  id(libs.plugins.kotlin.kapt.get().pluginId)
  id(libs.plugins.kotlin.parcelize.get().pluginId)
  id(libs.plugins.kotlin.binary.compatibility.get().pluginId)
  id(libs.plugins.nexus.plugin.get().pluginId)
}

apply(from = "${rootDir}/scripts/publish-module.gradle.kts")

mavenPublishing {
  val artifactId = "bindables"
  coordinates(
    Configuration.artifactGroup,
    artifactId,
    rootProject.extra.get("libVersion").toString()
  )

  pom {
    name.set(artifactId)
    description.set("Android DataBinding kit for notifying data changes to UI layers with MVVM architecture.")
  }
}

android {
  compileSdk = Configuration.compileSdk
  namespace = "com.skydoves.bindables"
  defaultConfig {
    minSdk = Configuration.minSdk
    //consumerProguardFiles("consumer-rules.pro")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
    freeCompilerArgs += listOf("-Xexplicit-api=strict")
  }

  buildFeatures {
    dataBinding = true
  }
}

apiValidation {
  ignoredPackages.add("com/skydoves/bindables/databinding")
  nonPublicMarkers.add("kotlin.PublishedApi")
}

dependencies {
  implementation(libs.kotlin.reflect)
  implementation(libs.androidx.material)

  api(libs.androidx.lifecycle.viewmodel.ktx)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
}