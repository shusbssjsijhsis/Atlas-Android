# AtlasAndroid 🚀

A modern Android starter project built with **Kotlin 2.0** and **Jetpack Compose**, featuring a clean Gradle setup and **GitHub Actions CI** for automated builds.

![Android](https://img.shields.io/badge/platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blueviolet)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-blue)
![CI](https://img.shields.io/github/actions/workflow/status/shusbssjsijhsis/AndroidStarter/android-ci.yml)

---

## ✨ Features

- 🟢 **Kotlin 2.0**
- 🎨 **Jetpack Compose** (declarative UI)
- 🧱 **Modern Gradle setup** (Version Catalog + Wrapper)
- 🤖 **GitHub Actions CI**
  - Automatic `assembleDebug`
  - APK artifact available for download
- 📦 Clean project structure, easy to extend

---

## 🧭 Project Structure

```text
AndroidStarter/
├── app/                    # Application module
│   ├── src/main/java/      # Kotlin source code
│   ├── src/main/res/       # Resources
│   └── build.gradle.kts
├── gradle/
│   └── wrapper/            # Gradle wrapper
├── .github/workflows/      # GitHub Actions CI
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
