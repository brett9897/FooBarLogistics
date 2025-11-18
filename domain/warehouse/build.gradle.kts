plugins {
    kotlin("jvm")
}

group = "com.foobarlogistics.domain.warehouse"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotestRunner)
    testImplementation(libs.kotestAssertions)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}