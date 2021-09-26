plugins {
    kotlin("jvm") version "1.5.31"
    java
}

group = "io.sfx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}