plugins {
    kotlin("jvm")
    `java-library`
}

group = "com.foobarlogistics.infrastructure.database.warehouse"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.flyway.core)
    testImplementation(kotlin("test"))

    runtimeOnly(libs.flyway.database.postgresql)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}