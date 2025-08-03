dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.starter.gateway)
    implementation(libs.spring.cloud.starter.consul.discovery)
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
}