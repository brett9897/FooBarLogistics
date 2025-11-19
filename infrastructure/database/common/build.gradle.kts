plugins {
    kotlin("jvm")
}

group = "foobarlogistics.infrastructure.database.common"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.exposed.core)
    implementation(libs.exposed.kotlin.datetime)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}