import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    id("com.github.johnrengelman.shadow") version "8.1.1"

}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.apache.openwebbeans:openwebbeans-gradle:2.0.27")
    implementation("org.apache.openwebbeans:openwebbeans-gradle:2.0.27")
    implementation("org.apache.openwebbeans:openwebbeans-gradle:2.0.27")
    implementation("org.apache.openwebbeans:openwebbeans-gradle:2.0.27")
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Use the JUnit 5 integration.
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")

    implementation ("com.google.code.gson:gson:2.10.1")


}


// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("MainKt")
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
    args("")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

