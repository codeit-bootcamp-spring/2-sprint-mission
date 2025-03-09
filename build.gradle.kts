plugins {
    id("java")
}

group = "discodeit"
version = "1.0-SNAPSHOT"

// Java 버전 설정 (Java 17로 설정)
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

// 출력 디렉토리 설정
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}