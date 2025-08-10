plugins {
    id("org.sonarqube") version "6.2.0.5505"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

sonar {
    properties {
        property("sonar.projectName", "codebox")
        property("sonar.host.url", "https://sonarqube.dornol.dev")
    }
}