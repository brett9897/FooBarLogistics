plugins {
    kotlin("jvm")
}

group = "com.foobarlogistics.application"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain:warehouse"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}