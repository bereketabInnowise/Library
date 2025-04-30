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
    implementation(libs.spring.context)
    implementation(libs.spring.aop)
    implementation(libs.aspectjweaver)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.csv)

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

//create fully functional .jar file
tasks.jar {
    manifest {
        attributes["Main-Class"] = "library.LibraryApplication"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Define CSV path as a property for injection
extra["csvPath"] = "src/main/resources/books.csv"