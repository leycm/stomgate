dependencies {
    implementation(project(":api"))
    implementation(libs.leyneck)
    compileOnly(libs.jetanno)

    compileOnly(libs.minestom)
    testImplementation(libs.minestom)
    testImplementation(libs.logback)
}

tasks.named("sourcesJar") {
    mustRunAfter(":api:jar")
}
