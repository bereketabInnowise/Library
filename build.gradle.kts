plugins {
    id("java")
}

group = "library"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
//org.springframework:spring-context:6.1.5
dependencies {
    implementation("org.springframework:spring-context:6.0.11")
    implementation("org.springframework:spring-aop:6.0.11")
    implementation("org.aspectj:aspectjweaver:1.9.19")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.16.1")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "library.LibraryApplication"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE  // Handle duplicate files
}
// Define CSV path as a property for injection
extra["csvPath"] = "src/main/resources/books.csv"
