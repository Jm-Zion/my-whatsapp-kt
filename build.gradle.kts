buildscript {
    extra.apply{
        set("compose_ui_version","1.1.1")
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}