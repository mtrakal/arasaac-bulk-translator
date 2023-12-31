plugins {
    application
    alias(libs.plugins.plugin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktlint)
}

group = "cz.mtrakal.arasaac.bulktranslator"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.converter)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.retrofit)

    implementation(libs.logging)
    implementation(libs.slf4j.simple)

    implementation(libs.sandwich)
    implementation(libs.sandwich.retrofit)
    implementation(libs.sandwich.retrofit.serialization)

    // https://mvnrepository.com/artifact/org.apache.poi/poi
    implementation(libs.apache.poi)
    implementation(libs.apache.poi.ooxml)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.koin.test) {
        exclude("junit", "junit")
        exclude("org.jetbrains.kotlin", "kotlin-test-junit")
    }
    testImplementation(libs.koin.test.junit) {
        exclude("junit", "junit")
        exclude("org.jetbrains.kotlin", "kotlin-test-junit")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
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
