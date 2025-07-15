plugins {
    id("java")
    id("maven-publish")
}

group = "dev.dornol.codebox.excelutil"
version = "1.0"

java {
    withSourcesJar()
    withJavadocJar()
}

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

tasks.javadoc {
    options.encoding = "UTF-8"
    (options as? org.gradle.external.javadoc.StandardJavadocDocletOptions)?.apply {
        encoding = "UTF-8"
        charSet = "UTF-8"
        docEncoding = "UTF-8"
    }
}