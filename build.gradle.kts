plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")

        classpath("com.android.tools.build:gradle:8.2.2")
    }
}
