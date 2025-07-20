plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.poi:poi-ooxml:5.4.1")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("org.hibernate.validator:hibernate-validator:9.0.1.Final")
}

tasks.test {
    useJUnitPlatform()
}