
plugins {
    id("application")
    id("java")
    kotlin("jvm") version "1.9.21"
}

group = "me.samdc"
version = "1.0-SNAPSHOT"

configure<ApplicationPluginConvention> {
    mainClassName = "com.example.Main"
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.jakewharton.picnic:picnic:0.7.0")
    implementation("com.mitchtalmadge:ascii-data:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.nd4j:nd4j-native-platform:1.0.0-M2.1")
    implementation(kotlin("script-runtime"))

}

tasks.test {
    useJUnit()
}

tasks.withType<JavaCompile>() {
//    sourceCompatibility = "17"
//    targetCompatibility = "17"
}

//tasks.withType<KotlinCompile>() {
//    kotlinOptions.jvmTarget = "17"
//}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "17"
//}
//val compileTestKotlin: KotlinCompile by tasks
//compileTestKotlin.kotlinOptions {
//    jvmTarget = "17"
//}