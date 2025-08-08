plugins {
    java
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
}

group = "library"
version = "1.0.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release")}
    }
}

// Remove dependency block entirely
subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    tasks.test {
        useJUnitPlatform()
        jvmArgs("-Xmx512m")
    }
}
