set -e

ROOT_NAME="AndroidStarter"
PKG="com.atlas.android"
APP_LABEL="Atlas Android"
PKG_PATH="$(echo "$PKG" | tr "." "/")"

git config user.name  "${GITHUB_USER:-shusbssjsijhsis}"
git config user.email "${GITHUB_USER:-user}@users.noreply.github.com"

cat > .gitignore <<'EOF'
*.iml
.gradle/
.idea/
.DS_Store
/build
**/build/
local.properties
captures/
.externalNativeBuild/
.cxx/
*.keystore
EOF

cat > README.md <<EOF
# $ROOT_NAME

A clean Android starter project (Kotlin + Jetpack Compose).
Package: \`$PKG\`
EOF

cat > settings.gradle.kts <<EOF
pluginManagement { repositories { google(); mavenCentral(); gradlePluginPortal() } }
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories { google(); mavenCentral() }
}
rootProject.name = "$ROOT_NAME"
include(":app")
EOF

cat > build.gradle.kts <<'EOF'
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.android) apply false
}
EOF

cat > gradle.properties <<'EOF'
org.gradle.jvmargs=-Xmx3g -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
EOF

mkdir -p gradle
cat > gradle/libs.versions.toml <<'EOF'
[versions]
agp = "8.5.2"
kotlin = "2.0.20"
composeBom = "2024.10.00"

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }

[libraries]
androidx-core-ktx = { module = "androidx.core:core-ktx", version = "1.13.1" }
androidx-activity-compose = { module = "androidx.activity:activity-compose", version = "1.9.3" }
compose-bom = { module = "androidx.compose:compose-bom", version.ref = "composeBom" }
compose-ui = { module = "androidx.compose.ui:ui" }
compose-ui-tooling = { module = "androidx.compose.ui:ui-tooling" }
compose-ui-tooling-preview = { module = "androidx.compose.ui:ui-tooling-preview" }
compose-material3 = { module = "androidx.compose.material3:material3" }
EOF

rm -rf app
mkdir -p app/src/main/java/"$PKG_PATH"
mkdir -p app/src/main/res/values

cat > app/build.gradle.kts <<EOF
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "$PKG"
  compileSdk = 34

  defaultConfig {
    applicationId = "$PKG"
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
  composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }
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
EOF

cat > app/src/main/AndroidManifest.xml <<EOF
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
  <application
    android:label="$APP_LABEL"
    android:theme="@android:style/Theme.DeviceDefault.Light.NoActionBar">
    <activity android:name="$PKG.MainActivity" android:exported="true">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
    </activity>
  </application>
</manifest>
EOF

cat > app/src/main/java/"$PKG_PATH"/MainActivity.kt <<EOF
package $PKG

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface { AppScreen() }
      }
    }
  }
}

@Composable
private fun AppScreen() {
  var text by remember { mutableStateOf("") }
  val items = remember { mutableStateListOf<String>() }

  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("$APP_LABEL", style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(12.dp))

    Row(Modifier.fillMaxWidth()) {
      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.weight(1f),
        label = { Text("New item") }
      )
      Spacer(Modifier.width(8.dp))
      Button(onClick = {
        val v = text.trim()
        if (v.isNotEmpty()) items.add(0, v)
        text = ""
      }) { Text("Add") }
    }

    Spacer(Modifier.height(16.dp))
    items.forEach { Text("• $it") }
  }
}
EOF

cat > app/src/main/res/values/strings.xml <<EOF
<resources>
  <string name="app_name">$APP_LABEL</string>
</resources>
EOF

# Gradle wrapper
GRADLE_VER="8.7"
curl -fsSL -o /tmp/gradle.zip "https://services.gradle.org/distributions/gradle-\${GRADLE_VER}-bin.zip"
unzip -q /tmp/gradle.zip -d /tmp
/tmp/gradle-\${GRADLE_VER}/bin/gradle wrapper --gradle-version \${GRADLE_VER}
rm -rf /tmp/gradle.zip /tmp/gradle-\${GRADLE_VER}

# simple CI (no build step yet; ensures workflow runs)
mkdir -p .github/workflows
cat > .github/workflows/ci.yml <<'EOF'
name: CI
on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]
jobs:
  smoke:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: echo "Repo initialized"
EOF

git add .
git commit -m "chore: initialize Android project" || true
git push
echo "DONE"
