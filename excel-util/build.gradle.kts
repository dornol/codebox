plugins {
    id("java")
    id("maven-publish")
}

group = "dev.dornol.codebox.excelutil"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.poi:poi-ooxml:5.4.1")
    implementation("ch.qos.logback:logback-classic:1.5.18")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "dev.dornol.codebox.excelutil"
            artifactId = "excelutil"
            version = "1.0"
        }
    }

    repositories {
        mavenLocal()
    }
}

tasks.test {
    useJUnitPlatform()
}