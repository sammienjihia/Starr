plugins {
    `kotlin-dsl`
    id ("org.openapi.generator") version "4.3.1"
}

repositories {
    jcenter()
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.openapitools:openapi-generator-gradle-plugin:4.3.1")
}

