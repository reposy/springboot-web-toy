plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("kapt") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "springboot-kotlin"
version = "0.0.1-SNAPSHOT"

noArg {
    annotation("jakarta.persistence.Entity") // @Entity 어노테이션에서 no-arg 생성자 생성
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // H2 Database
    runtimeOnly("com.h2database:h2")

    // jackson module kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

    // jsonwebtoken
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")

    // Spring Boot Starter
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // mockk
    testImplementation("io.mockk:mockk:1.13.16")

    // Junit
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}