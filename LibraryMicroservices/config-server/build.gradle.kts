dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.config.server)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.micrometer)
    implementation(libs.micrometer.core)
}
repositories {
    mavenCentral()
}