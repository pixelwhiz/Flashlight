plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.pixelwhiz"
version = "1.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
}


dependencies {
    compileOnly("com.github.PowerNukkitX:PowerNukkitX:master-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveFileName.set("Flashlight.jar")
    destinationDirectory.set(file("../../"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    processResources {
        filteringCharset = "UTF-8"
    }
}