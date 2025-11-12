plugins {
    kotlin("jvm")
}

group = "warehouse-management.application-tests"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(project(":application"))

    testImplementation(libs.bundles.cucumber)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

    systemProperty("cucumber.plugin", "pretty, html:build/reports/cucumber.html, json:build/reports/cucumber.json")
}
kotlin {
    jvmToolchain(21)
}