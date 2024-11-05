import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaVersion = JavaVersion.VERSION_21
val kotlinJVM = JvmTarget.JVM_21

plugins {
    kotlin("jvm") version "2.0.21"
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
    testImplementation("io.cucumber:cucumber-java:7.20.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    dependsOn("ktlintCheck")
    compilerOptions {
        this.freeCompilerArgs.add("-Xjsr305=strict")
        this.jvmTarget.set(kotlinJVM)
    }
}

tasks.register<JavaExec>("cucumber") {
    group = "cucumber"
    dependsOn(tasks.testClasses)
    classpath(sourceSets.test.get().runtimeClasspath)

    this.mainClass.set("io.cucumber.core.cli.Main")
    this.args("--plugin", "pretty")
    println("path = ${sourceSets.test.get().resources.srcDirs.first()}")
    this.args("--glue", "io.github.soat7.myburgercontrol.testbdd", sourceSets.test.get().resources.srcDirs.first())
}

ktlint {
    this.coloredOutput.set(true)
    this.outputToConsole.set(true)
}
