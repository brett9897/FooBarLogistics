plugins {
    kotlin("jvm")
}

group = "warehouse-management.test-fixtures"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:warehouse"))
    implementation(project(":application"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}