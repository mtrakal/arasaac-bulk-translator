plugins {
    kotlin("jvm") version "1.9.0"
    application
    kotlin("plugin.serialization") version "1.6.0"
}

group = "cz.mtrakal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // https://mvnrepository.com/artifact/com.deepl.api/deepl-java
    implementation("com.deepl.api:deepl-java:1.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}