plugins {
    java
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
//    id("org.graalvm.buildtools.native") version "0.9.19"
}

group = "ru.itsyn"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { url = uri("https://repo.spring.io/milestone") }
    mavenCentral()
}

//extra["springCloudVersion"] = "2022.0.0"
//extra["testcontainersVersion"] = "1.17.6"

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
//    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
//    implementation("org.springframework.kafka:spring-kafka")

    implementation("com.rometools:rome:1.18.0")
//    implementation("org.telegram:telegrambots:6.4.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

//    runtimeOnly("com.h2database:h2")
//    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
//    testImplementation("org.springframework.kafka:spring-kafka-test")

//    testImplementation("org.testcontainers:junit-jupiter")
//    testImplementation("org.testcontainers:kafka")
//    testImplementation("org.testcontainers:mongodb")
//    testImplementation("org.testcontainers:postgresql")
}

dependencyManagement {
    imports {
//        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
//        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
