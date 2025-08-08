dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.config.server)
    implementation(libs.spring.boot.starter.web)
}
repositories {
    mavenCentral()
}