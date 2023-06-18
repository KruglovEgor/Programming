import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("org.openjfx.javafxplugin") version "0.0.10"

}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

group = "me.fk724s"
version = "1.0-SNAPSHOT"

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

    implementation("commons-codec:commons-codec:1.15")


    implementation("org.openjfx:javafx-controls:15.0.1")
    implementation("org.openjfx:javafx-fxml:15.0.1")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("org.controlsfx:controlsfx:11.1.0")
}


application {
    // Define the main class for the application.
    mainClass.set("client.MainKt")
}

javafx {
    version = "17.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
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

sourceSets {
    main{
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
