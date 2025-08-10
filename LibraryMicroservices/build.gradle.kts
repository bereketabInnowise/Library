plugins {
    java
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
    id("checkstyle")
}

group = "library"
version = "1.0.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release")}
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "checkstyle")

    checkstyle {
        toolVersion = "10.12.0"
        configFile = file("${rootProject.rootDir}/config/checkstyle/checkstyle.xml")
        configProperties = mapOf(
            "config_dir" to "${rootProject.rootDir}/config/checkstyle"
        )
    }

    tasks.test {
        useJUnitPlatform()
        jvmArgs("-Xmx512m")
    }
}

// Remove the checkstyle configuration from root level