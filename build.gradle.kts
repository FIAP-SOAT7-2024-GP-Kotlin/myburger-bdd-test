import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.IOException
import java.util.Properties

val javaVersion = JavaVersion.VERSION_21
val kotlinJVM = JvmTarget.JVM_21

val cucumberVersion = "7.20.1"
val fakerVersion = "1.16.0"
val restAassuredVersion = "5.5.0"

val props = Properties()
try {
    props.load(file("$projectDir/.env").inputStream())
} catch (e: IOException) {
    println(e.message)
}

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.allopen") version "2.0.21"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "io.github.soat7"
version = "0.0.1-SNAPSHOT"

if (!javaVersion.isCompatibleWith(JavaVersion.current())) {
    error(
        """
        =======================================================
        RUN WITH JAVA $javaVersion
        =======================================================
        """.trimIndent(),
    )
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.oshai:kotlin-logging:7+")
    implementation("ch.qos.logback:logback-classic:1+")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")

    implementation("io.rest-assured:rest-assured:$restAassuredVersion")
    implementation("io.rest-assured:json-path:$restAassuredVersion")
    implementation("io.rest-assured:kotlin-extensions:$restAassuredVersion")

    implementation("io.jsonwebtoken:jjwt-api:0.12+")
    implementation("io.jsonwebtoken:jjwt-impl:0.12+")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12+")

    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-picocontainer:$cucumberVersion")
    testImplementation("org.junit.platform:junit-platform-console")
    testImplementation("org.junit.platform:junit-platform-suite")
    testImplementation("io.github.serpro69:kotlin-faker:$fakerVersion")
    testImplementation("org.assertj:assertj-core:3+")

}

tasks.test {
    useJUnitPlatform()
    systemProperty("cucumber.junit-platform.naming-strategy", "long")
    environment.putAll(
        props.entries.associate { it.key.toString() to it.value.toString() },
    )
}

tasks.withType<KotlinCompile> {
//    dependsOn("ktlintCheck")
    compilerOptions {
        this.freeCompilerArgs.add("-Xjsr305=strict")
        this.jvmTarget.set(kotlinJVM)
    }
}

java {
    sourceCompatibility = javaVersion
}

ktlint {
    this.coloredOutput.set(true)
    this.outputToConsole.set(true)
}
