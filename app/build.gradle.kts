plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  id("org.jetbrains.kotlin.plugin.compose")
}

import java.util.Properties

android {
  namespace = "com.atlas.android"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.atlas.android"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = "17" }

  buildFeatures { compose = true }

  // ---- Signing (for Release) ----
  val props = Properties()
  val propsFile = rootProject.file("keystore.properties")
  if (propsFile.exists()) {
    propsFile.inputStream().use { props.load(it) }
  }

  signingConfigs {
    create("release") {
      val ksFile = props.getProperty("storeFile") ?: ""
      if (ksFile.isNotBlank()) {
        storeFile = file(ksFile)
        storePassword = props.getProperty("storePassword")
        keyAlias = props.getProperty("keyAlias")
        keyPassword = props.getProperty("keyPassword")
      }
    }
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
    }
    getByName("release") {
      isMinifyEnabled = false
      signingConfig = signingConfigs.getByName("release")
    }
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.activity.compose)

  implementation(platform(libs.compose.bom))
  implementation(libs.compose.ui)
  implementation(libs.compose.material3)
  implementation(libs.compose.ui.tooling.preview)
  debugImplementation(libs.compose.ui.tooling)
}
