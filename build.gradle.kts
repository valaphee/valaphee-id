/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

import com.github.gradle.node.task.NodeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.node-gradle.node") version "3.1.1"
    id("com.palantir.git-version") version "0.12.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    id("net.linguica.maven-settings") version "0.5"
    id("org.springframework.boot") version "2.6.3"
    kotlin("plugin.jpa") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    signing
}

group = "com.valaphee"
val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
val details = versionDetails()
version = "${details.lastTag}.${details.commitDistance}"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("mysql:mysql-connector-java")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.passay:passay:1.6.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    if (!project.hasProperty("production")) implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure:2.6.3")
    implementation("com.nimbusds:nimbus-jose-jwt:9.19")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "15"
        targetCompatibility = "15"
    }

    withType<KotlinCompile> { kotlinOptions { jvmTarget = "15" } }

    withType<Test> { useJUnitPlatform() }

    register<NodeTask>("buildReactApp") {
        dependsOn("npmInstall")
        script.set(project.file("node_modules/webpack/bin/webpack.js"))
        args.set(listOf(
            "--config", "webpack.${if (project.hasProperty("production")) "production" else "development"}.js",
            "-o", "./src/main/resources/static"
        ))
    }

    processResources { dependsOn("buildReactApp") }

    bootBuildImage {
        builder = "paketobuildpacks/builder:0.0.464-base"
        imageName = "valaphee/valaphee-id:${project.version}"
    }

    clean { delete(file("node_modules")) }
}

signing { useGpgCmd() }
