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
    testImplementation(project(":test-fixtures"))
    testImplementation(project(":domain:warehouse"))

    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation(libs.bundles.cucumber)
    testImplementation("io.cucumber:cucumber-picocontainer:7.18.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

    systemProperty("cucumber.plugin", "pretty, html:build/reports/cucumber.html, json:build/reports/cucumber.json")
}
kotlin {
    jvmToolchain(21)
}